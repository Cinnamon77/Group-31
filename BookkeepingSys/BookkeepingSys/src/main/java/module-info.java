module bookkeeping.system.bookkeepingsys {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;

    opens bookkeeping.system.bookkeepingsys to javafx.fxml;
    opens bookkeeping.system.bookkeepingsys.controller to javafx.fxml;
    
    exports bookkeeping.system.bookkeepingsys;
    exports bookkeeping.system.bookkeepingsys.controller;
    exports bookkeeping.system.bookkeepingsys.model;
    exports bookkeeping.system.bookkeepingsys.util;
    exports bookkeeping.system.bookkeepingsys.deepseekTest;
    opens bookkeeping.system.bookkeepingsys.deepseekTest to javafx.fxml;
} 