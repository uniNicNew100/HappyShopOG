package ci553.happyshop.client.picker;

import ci553.happyshop.client.Main;

import java.io.IOException;

public class PickerController {
    public PickerModel pickerModel;
    public Main main;
    public PickerController(Main main) {
        this.main = main;
    }

    public void doProgressing() throws IOException {
        pickerModel.doProgressing();
    }
    public void doCollected() throws IOException {
        pickerModel.doCollected();
    }

    public void doMenu()  {
        main.startEmployeeMenu();
    }
}
