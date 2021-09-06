package View.MainScene.NewEntry.EntryTypes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ccNewEntryController {

    @FXML TextField ccNumberNew;

    @FXML
    protected void initialize() throws IOException
    {
        /** Listener to detect out of focus textField */
        ccNumberNew.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    ccNumberFormat();
                }
            }
        });
    }
    public void ccNumberValidate(KeyEvent e)
    {
        TextField ccNumberTextField = (TextField) e.getSource();
        String temp = ccNumberTextField.getText();
        int caretPos = ccNumberTextField.getCaretPosition();
        if(temp.equals("")){return;}

        /** Filtering any characters that are not digits or dashes */
        ArrayList<String> textFieldChars = new ArrayList(Arrays.asList(temp.split("")));
        //if(textFieldChars.size() == 17){textFieldChars.remove(16);}
        for(int i = 0; i < textFieldChars.size(); i++)
        {
            if(!textFieldChars.get(i).matches("\\d|-"))
            {
                textFieldChars.remove(i);
            }
        }

        String validatedStr = String.join("", textFieldChars);
        ccNumberTextField.setText(validatedStr);
        ccNumberTextField.positionCaret(caretPos);
    }
    public void ccNumberFormat()
    {
        String ccNumb = ccNumberNew.getText();

        if(ccNumb.length() >= 16 && !ccNumb.matches("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}$"))
        {
            /** If the format of the number is not correct, it is fixed */
            String s = ccNumb.replaceAll("-", "");;
            ccNumb = s.substring(0, 4) + "-" + s.substring(4, 8)+ "-" + s.substring(8, 12)+ "-" + s.substring(12, 16);
            ccNumberNew.setText(ccNumb);
        }
    }
    public void expMonthValidate(KeyEvent e)
    {
        TextField expMonthTextField = (TextField) e.getSource();
        String temp = expMonthTextField.getText();
        if(temp.equals("")){return;}

        ArrayList<String> textFieldChars = new ArrayList(Arrays.asList(temp.split("")));
        if(textFieldChars.size() == 3){textFieldChars.remove(2);}

        for(int i = 0; i < textFieldChars.size(); i++)
        {
            if(!textFieldChars.get(i).matches("\\d"))
            {
                textFieldChars.remove(i);
            }
        }

        String validatedStr = textFieldChars.size() > 0 ? String.join("", textFieldChars) : "99";
        if(Integer.parseInt(validatedStr) > 12){validatedStr = "";}
        int caretPos = expMonthTextField.getCaretPosition();
        expMonthTextField.setText(validatedStr);
        expMonthTextField.positionCaret(caretPos);
    }
    public void expYearValidate(KeyEvent e)
    {
        TextField expMonthTextField = (TextField) e.getSource();
        String temp = expMonthTextField.getText();
        if(temp.equals("")){return;}

        ArrayList<String> textFieldChars = new ArrayList(Arrays.asList(temp.split("")));
        if(textFieldChars.size() == 3){textFieldChars.remove(2);}

        for(int i = 0; i < textFieldChars.size(); i++)
        {
            if(!textFieldChars.get(i).matches("\\d"))
            {
                textFieldChars.remove(i);
            }
        }

        String validatedStr = textFieldChars.size() > 0 ? String.join("", textFieldChars) : "99";
        int year = Integer.parseInt(validatedStr);

        int currentYear =  Calendar.getInstance().get(Calendar.YEAR) % 100;
        boolean validity = year >= currentYear && year <= currentYear + 5;

        if(!validity && year > 9){validatedStr = "";}
        int caretPos = expMonthTextField.getCaretPosition();
        expMonthTextField.setText(validatedStr);
        expMonthTextField.positionCaret(caretPos);
    }
    public void ccv2Validate(KeyEvent e)
    {
        TextField expYearTextField = (TextField) e.getSource();
        String temp = expYearTextField.getText();
        if(temp.equals("")){return;}

        ArrayList<String> textFieldChars = new ArrayList(Arrays.asList(temp.split("")));
        if(textFieldChars.size() == 4){textFieldChars.remove(3);}

        for(int i = 0; i < textFieldChars.size(); i++)
        {
            if(!textFieldChars.get(i).matches("\\d"))
            {
                textFieldChars.remove(i);
            }
        }

        String validatedStr = String.join("", textFieldChars);
        int caretPos = expYearTextField.getCaretPosition();
        expYearTextField.setText(validatedStr);
        expYearTextField.positionCaret(caretPos);
    }
    public void nameValidate(KeyEvent e)
    {
        TextField nameTextField = (TextField) e.getSource();
        String temp = nameTextField.getText();
        if(temp.equals("")){return;}

        ArrayList<String> textFieldChars = new ArrayList(Arrays.asList(temp.split("")));

        for(int i = 0; i < textFieldChars.size(); i++)
        {
            if(!textFieldChars.get(i).matches("^([a-zA-Z ']*)$"))
            {
                textFieldChars.remove(i);
            }
        }

        String validatedStr = String.join("", textFieldChars);
        if(!validatedStr.matches("^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)"))
        {
            nameTextField.setStyle("-fx-background-color: #ffcccc;");
        }
        else
        {
            nameTextField.setStyle("-fx-background-color: #d9d9d9;");
        }

        int caretPos = nameTextField.getCaretPosition();
        nameTextField.setText(validatedStr);
        nameTextField.positionCaret(caretPos);
    }
}
