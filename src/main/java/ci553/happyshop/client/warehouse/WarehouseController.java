package ci553.happyshop.client.warehouse;

import ci553.happyshop.client.Main;

import java.io.IOException;
import java.sql.SQLException;

public class WarehouseController {
    public WarehouseModel model;
    public Main main;
    public WarehouseController(Main main) {
        this.main = main;
    }

    void process(String action) throws SQLException, IOException {
        switch (action) {
            case "🔍":
                model.doSearch();
                break;
            case "Edit":
                model.doEdit();
                break;
            case "Delete":
                model.doDelete();
                break;
            case "➕":
                model.doChangeStockBy("add");
                break;
            case "➖":
                model.doChangeStockBy("sub");
                break;
            case "Submit":
                model.doSummit();
                break;
            case "Cancel":  // clear the editChild
                model.doCancel();
                break;
            case "Menu":
                main.startEmployeeMenu();
        }
    }
}
