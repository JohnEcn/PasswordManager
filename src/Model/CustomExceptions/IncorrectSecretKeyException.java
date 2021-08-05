package Model.CustomExceptions;

public class IncorrectSecretKeyException extends Exception{

    public IncorrectSecretKeyException(String message) {
        super(message);
    }
}
