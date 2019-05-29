import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

import java.util.Random;

@RunWith(JUnit4.class)
public class TaskWindowControllerTest {

    private TextField textField;
    private TextArea descTextArea;
    private TaskWindowController twc;

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Before
    public void prepare() {
        twc = new TaskWindowController(null);
        twc.titleTextField = new TextField();
        twc.descTextArea = new TextArea();
    }

    @Test
    public void testTitleInput() {
        twc.titleTextField.setText(generateString(0));
        Assert.assertFalse(twc.titleLengthCheck());

        twc.titleTextField.setText(generateString(50));
        Assert.assertTrue(twc.titleLengthCheck());

        twc.titleTextField.setText(generateString(256));
        Assert.assertFalse(twc.titleLengthCheck());

        twc.titleTextField.setText(generateString(1000));
        Assert.assertFalse(twc.titleLengthCheck());
    }

    @Test
    public void testDescriptionInput() {
        twc.descTextArea.setText(generateString(0));
        Assert.assertTrue(twc.descriptionLengthCheck());

        twc.descTextArea.setText(generateString(500));
        Assert.assertTrue(twc.descriptionLengthCheck());

        twc.descTextArea.setText(generateString(1024));
        Assert.assertFalse(twc.descriptionLengthCheck());

        twc.descTextArea.setText(generateString(2000));
        Assert.assertFalse(twc.descriptionLengthCheck());
    }


    private String generateString(int length) {
        int leftLimit = 32; // from ' ' through all letters and special characters
        int rightLimit = 126; // to '~'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char)randomLimitedInt);
        }
        return buffer.toString();
    }
}