module com.gestion_presence.gestion_presence {
    requires javafx.controls;
    requires javafx.fxml;

    requires static lombok;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.mail;
    requires kernel;
    requires layout;

    requires jxl;


    opens com.gestion_presence.gestion_presence.Models to org.hibernate.orm.core, javafx.base;

    opens com.gestion_presence.gestion_presence.utils;
    exports com.gestion_presence.gestion_presence.utils;


    opens com.gestion_presence.gestion_presence to javafx.fxml;
    exports com.gestion_presence.gestion_presence;
}