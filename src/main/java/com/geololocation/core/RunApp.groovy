package com.geololocation.core

class RunApp {
    static void main(String[] args) {
         try {
            ConsoleCommunicator communicator = new ConsoleCommunicator()
            communicator.searchStart()
        }
        catch (IOException ie){
            ie.printStackTrace()
        }
        catch (Throwable ex){
            ex.printStackTrace()
        }
    }
}