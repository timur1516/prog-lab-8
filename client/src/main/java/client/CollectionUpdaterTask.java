package client;

import client.Controllers.CollectionController;
import client.GUI.MainForm;
import common.net.requests.ClientRequest;

/**
 * Task of collection updater
 * It calls update collection method of Collection controller and if collection changed, data table is updated too
 */
public class CollectionUpdaterTask extends Thread{
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if(ClientRequest.getUser() == null) continue;
            try {
                if(CollectionController.getInstance().updateCollection()){
                    MainForm.getInstance().updateDataTable(CollectionController.getInstance().getProcessedCollection());
                }
            } catch (Exception ignored) {}
        }
    }
}
