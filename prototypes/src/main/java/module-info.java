module com.example.prototypes {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.sql.rowset;
    requires org.postgresql.jdbc;

    opens com.example.prototypes to javafx.fxml;
    exports com.example.prototypes;
}