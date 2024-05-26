package client.Controllers;

import client.Commands.ShowCommand;
import common.Collection.Worker;
import common.net.requests.ResultState;
import common.net.requests.ServerResponse;
import common.utils.CommonConstants;

import java.util.*;
import java.util.function.Predicate;

public class CollectionController {
    private Collection<Worker> collection;
    private Predicate<Worker> filter;
    private Comparator<Worker> comparator;
    private static CollectionController COLLECTION_CONTROLLER = null;

    public static synchronized CollectionController getInstance(){
        if(COLLECTION_CONTROLLER == null){
            COLLECTION_CONTROLLER = new CollectionController();
        }
        return COLLECTION_CONTROLLER;
    }
    private CollectionController(){
        filter = o -> true;
        comparator = Comparator.comparing(Worker::getName);
        collection = new ArrayList<>();
    };

    public boolean updateCollection() throws Exception {
        ServerResponse response = (new ShowCommand()).execute();
        if(response.state() == ResultState.EXCEPTION){
            throw (Exception) response.data();
        }
        Collection<Worker> newCollection = (Collection<Worker>) response.data();

        if(collection == null){
            collection = newCollection;
            return true;
        }

        if(newCollection.stream().toList().equals(collection.stream().toList())) return false;
        collection = newCollection;
        return true;
    }
    public Collection<Worker> getProcessedCollection(){
        return collection.stream().filter(filter).sorted(comparator).toList();
    }
    public Collection<Worker> getCollection(){
        return collection;
    }

    public void setFilter(String fieldName, String value){
        filter = switch (fieldName){
            case "id" -> worker -> String.valueOf(worker.getId()).equals(value);
            case "name" -> worker -> String.valueOf(worker.getName()).equals(value);
            case "x" -> worker -> String.valueOf(worker.getCoordinates().getX()).equals(value);
            case "y" -> worker -> String.valueOf(worker.getCoordinates().getY()).equals(value);
            case "creationDate" -> worker -> (worker.getCreationDate().format(CommonConstants.formatter)).equals(value);
            case "salary" -> worker -> String.valueOf(worker.getSalary()).equals(value);
            case "startDate" -> worker -> (worker.getStartDate().toLocalDate().format(CommonConstants.formatter)).equals(value);
            case "endDate" -> worker -> {
                if(worker.getEndDate() == null) return value.isEmpty();
                return (worker.getEndDate().toLocalDate().format(CommonConstants.formatter)).equals(value);
            };
            case "status" -> worker -> String.valueOf(worker.getStatus()).equals(value);
            case "height" -> worker -> {
                if(worker.getPerson() == null) return value.isEmpty();
                return String.valueOf(worker.getPerson().getHeight()).equals(value);
            };
            case "eyeColor" -> worker -> {
                if(worker.getPerson() == null) return value.isEmpty();
                if(worker.getPerson().getEyeColor() == null) return value.isEmpty();
                return String.valueOf(worker.getPerson().getEyeColor()).equals(value);
            };
            case "nationality" -> worker -> {
                if(worker.getPerson() == null) return value.isEmpty();
                if(worker.getPerson().getNationality() == null) return value.isEmpty();
                return String.valueOf(worker.getPerson().getNationality()).equals(value);
            };
            case "" -> worker -> true;
            default -> filter;
        };
    }

    public void setComparator(String fieldName){
        comparator = switch (fieldName){
            case "id" -> Comparator.comparingLong(Worker::getId);
            case "name", "" -> Comparator.comparing(Worker::getName);
            case "x" -> Comparator.comparingDouble(worker -> worker.getCoordinates().getX());
            case "y" -> Comparator.comparingDouble(worker -> worker.getCoordinates().getY());
            case "creationDate" -> Comparator.comparing(Worker::getCreationDate);
            case "salary" -> Comparator.comparingInt(Worker::getSalary);
            case "startDate" -> Comparator.comparing(Worker::getStartDate);
            case "endDate" -> Comparator.comparing(Worker::getEndDate, Comparator.nullsLast(Comparator.naturalOrder()));
            case "status" -> Comparator.comparing(Worker::getStatus);
            case "height" -> Comparator.comparing(worker -> worker.getPerson() == null ? null : worker.getPerson().getHeight(), Comparator.nullsLast(Comparator.naturalOrder()));
            case "eyeColor" -> Comparator.comparing(worker -> worker.getPerson() == null ? null : worker.getPerson().getEyeColor(), Comparator.nullsLast(Comparator.naturalOrder()));
            case "nationality" -> Comparator.comparing(worker -> worker.getPerson() == null ? null : worker.getPerson().getNationality(), Comparator.nullsLast(Comparator.naturalOrder()));
            default -> comparator;
        };
    }
}
