package Model.CustomExceptions;

public class NotUniqueEntryNameException extends Exception{

    public NotUniqueEntryNameException(String message) {
        super(message);
    }
}
