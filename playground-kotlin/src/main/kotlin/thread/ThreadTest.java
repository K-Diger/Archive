package main.kotlin.thread;

public class ThreadTest {

    public static void main(String[] args) {

        Thread mainThread = Thread.currentThread();  // 현재 쓰레드 얻기
        mainThread.setName("main-thread");  // 쓰레드 이름 설정
        System.out.println("=============================");
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                this.setName("work-thread-1");  // 쓰레드 이름 설정
                System.out.println("-----------------------------");
                for (int i = 0; i < 5; i++) {
                    System.out.print("하하 ");
                    System.out.println(this.getName()); // 쓰레드 이름 얻어 출력
                    try {
                        Thread.sleep(2000); // 2 second sleep
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread1.start(); // JVMachine calls the run method

        System.out.println("#############################");
        for (int i = 0; i < 5; i++) {
            System.out.print("호호 ");
            System.out.println(mainThread.getName()); // 쓰레드 이름 얻어 출력
            try {
                Thread.sleep(2000); // 2 second sleep
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
