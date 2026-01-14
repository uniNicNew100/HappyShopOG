package ci553.happyshop.client.catalogueBrowser;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class CatalogueClient extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();
        CatalogueModel catalogueModel = new CatalogueModel(databaseRW);
        CatalogueView catalogueView = new CatalogueView();
        CatalogueController catalogueController = new CatalogueController();

        catalogueController.model = catalogueModel;
        catalogueController.view = catalogueView;
        catalogueView.setController(catalogueController);

        catalogueController.loadAllProducts(); // initial load


        stage.setScene(new Scene(catalogueView.getRoot(), 600, 400));
        stage.setTitle("Product Catalogue");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
