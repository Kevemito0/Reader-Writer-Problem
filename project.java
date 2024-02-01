import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class project {
	public static void main(String [] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		ReadWriteLock RW = new ReadWriteLock();
		
		
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		executorService.execute(new Writer(RW));
		
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		executorService.execute(new Reader(RW));
		
		
	}
}


class ReadWriteLock{
	private Semaphore S=new Semaphore(1);
	private Semaphore S2 = new Semaphore(1);
	private int readCount = 0;

	
	public void readLock() {
		try{
			S2.acquire();
			readCount++;
			if(readCount == 1){
				S.acquire();
			}
			S2.release();
		}	catch (InterruptedException e ){
			e.printStackTrace();
		}
	}

	public void writeLock() {
		try {
			S.acquire();
		}	catch (InterruptedException e){
			e.printStackTrace();
		}
		
	}

	public void readUnLock() {
		
		try {
		S2.acquire();
		readCount--;
		if(readCount == 0){
			S.release();
			}
        S2.release();
		}	catch (InterruptedException e){
			e.printStackTrace();
		}
			
	}

	public void writeUnLock() {
		S.release();
			
	}

}




class Writer implements Runnable
{
   private ReadWriteLock RW_lock;

    public Writer(ReadWriteLock rw) {
    	RW_lock = rw;
   }

    public void run() {
      while (true){
    	RW_lock.writeLock();
    	RW_lock.writeUnLock();
      }
   }


}



class Reader implements Runnable
{
   private ReadWriteLock RW_lock;

   public Reader(ReadWriteLock rw) {
    	RW_lock = rw;
   }
    public void run() {
      while (true){ 	    
    	RW_lock.readLock();
    	RW_lock.readUnLock();
        
      }
   }


}