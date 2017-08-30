package evel_javalibrary.att.com;
/**************************************************************************//**
 * @file
 * RingBuffer class
 *
  * This file implements internal Ringbuffer for storing and
  *  forwarding events to Collector.
 *
 * License
 * -------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

import java.util.concurrent.Semaphore;
/*
 * Ringbuffer to store and Forward http(s) POST requests
 */
public class RingBuffer {

	    // message count semaphore
	    public static Semaphore countsem;
	    // space semaphore
	    public static Semaphore spacesem;
	    // lock semaphore
	    public static Semaphore lock;

	    public Object[] elements = null;

	    public int capacity  = 0;
	    public int writePos  = 0;
	    public int available = 0;

	    /*
	     * Constructs Ringbuffer of specified capacity
	     */
	    public RingBuffer(int capacity) {
	        this.capacity = capacity;
	        this.elements = new Object[capacity];
	        countsem = new Semaphore(1);
	        spacesem = new Semaphore(capacity);
	        lock = new Semaphore(1);
	    }

	    //resets the positions
	    public void reset() {
	        this.writePos = 0;
	        this.available = 0;
	    }

	    //returns available capacity
	    public int remainingCapacity() {
	        return this.capacity - this.available;
	    }



	    //Puts Java object into ringbuffer
	    public boolean put(Object element){
	    	
	    	boolean ret = false;
	    	//acquire locks
	    	try {
				spacesem.acquire();
				lock.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	

	    	//store object
	        if(available < capacity){
	            if(writePos >= capacity){
	                writePos = 0;
	            }
	            elements[writePos] = element;
	            writePos++;
	            available++;
	            ret = true;
	        }
	        
	    	//release Locks
		    lock.release();
			countsem.release();


	        return ret;
	    }

	    public int put(Object[] newElements){
	        return put(newElements, newElements.length);
	    }

	    public int put(Object[] newElements, int length){
	    	//Acquire locks
	    	try {
				spacesem.acquire();
				lock.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	        int readPos = 0;
	        if(this.writePos > this.available){
	            //space above writePos is all empty

	            if(length <= this.capacity - this.writePos){
	                //space above writePos is sufficient to insert batch

	                for(;  readPos < length; readPos++){
	                    this.elements[this.writePos++] = newElements[readPos];
	                }
	                this.available += readPos;
	                //release
	    		    lock.release();
	    			countsem.release();
	                return length;

	            } else {
	                //both space above writePos and below writePos is necessary to use
	                //to insert batch.

	                int lastEmptyPos = writePos - available;

	                for(; this.writePos < this.capacity; this.writePos++){
	                    this.elements[this.writePos] = newElements[readPos++];
	                }

	                //fill into bottom of array too.
	                this.writePos = 0;

	                int endPos = Math.min(length - readPos, capacity - available - readPos);
	                for(;this.writePos < endPos; this.writePos++){
	                    this.elements[this.writePos] = newElements[readPos++];
	                }
	                this.available += readPos;
	                //release
	    		    lock.release();
	    			countsem.release();
	                return readPos;
	            }
	        } else {
	            int endPos = this.capacity - this.available + this.writePos;

	            for(; this.writePos < endPos; this.writePos++){
	                this.elements[this.writePos] = newElements[readPos++];
	            }
	            this.available += readPos;
	            //release
			    lock.release();
				countsem.release();

	            return readPos;
	        }

	    }

	    /*
	     * Takes a stored object in Ringbuffer and releases the space
	     */

	    public Object take() {
	    	
	    	Object nextObj;
	    	//acquire lock
	    	try {
				countsem.acquire();
				lock.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	        if(available == 0){
	            nextObj = null;
	        }
	        else {
	          int nextSlot = writePos - available;
	          if(nextSlot < 0){
	            nextSlot += capacity;
	          }
	          nextObj = elements[nextSlot];
	          available--;
	        }
	        //releases object
		    lock.release();
			spacesem.release();
	        
	        return nextObj;
	    }


	    public int take(Object[] into){
	        return take(into, into.length);
	    }


	    public int take(Object[] into, int length){
	        int intoPos = 0;
	        
	    	//acquire lock
	    	try {
				countsem.acquire();
				lock.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        if(available <= writePos){
	            int nextPos= writePos - available;
	            int endPos   = nextPos + Math.min(available, length);

	            for(;nextPos < endPos; nextPos++){
	                into[intoPos++] = this.elements[nextPos];
	            }
	            this.available -= intoPos;
	            
	            //release
			    lock.release();
				countsem.release();
				
	            return intoPos;
	        } else {
	            int nextPos = writePos - available + capacity;

	            int leftInTop = capacity - nextPos;
	            if(length <= leftInTop){
	                //copy directly
	                for(; intoPos < length; intoPos++){
	                    into[intoPos] = this.elements[nextPos++];
	                }
	                this.available -= length;
		            //release
				    lock.release();
					countsem.release();
	                return length;

	            } else {
	                //copy top
	                for(; nextPos < capacity; nextPos++){
	                    into[intoPos++] = this.elements[nextPos];
	                }

	                //copy bottom - from 0 to writePos
	                nextPos = 0;
	                int leftToCopy = length - intoPos;
	                int endPos = Math.min(writePos, leftToCopy);

	                for(;nextPos < endPos; nextPos++){
	                    into[intoPos++] = this.elements[nextPos];
	                }

	                this.available -= intoPos;
	                
		            //release
				    lock.release();
					countsem.release();

	                return intoPos;
	            }
	        }
	    }
	
}
