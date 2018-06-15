/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.AnnoCount;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<AnnoCount> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    private Model model ;
    
    @FXML
    void handleAnalizza(ActionEvent event) {
    	String stato = boxStato.getValue();
    	if(stato == null) {
    		txtResult.appendText("ERRORE: devi selezionare uno stato\n");
    		return ;
    	}
    	List<String> successivi = model.getStatiSuccessivi(stato);
    	List<String> precedenti = model.getStatiPrecedenti(stato);
    	List<String> raggiungibili = model.getStatiRaggiungibili(stato);
    	
    	txtResult.appendText("\n\nSTATO DI PARTENZA: \n");
    	txtResult.appendText(stato+"\n");
    	txtResult.appendText("STATI SUCCESSIVI: \n");
    	txtResult.appendText(successivi.toString()+"\n");
    	txtResult.appendText("STATI PRECEDENTI: \n");
    	txtResult.appendText(precedenti.toString()+"\n");
    	txtResult.appendText("STATI RAGGIUNGIBILI: \n");
    	txtResult.appendText(raggiungibili.toString()+"\n");
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	AnnoCount anno = boxAnno.getValue() ;
    	
    	try {
        	model.createGraph(anno.getAnno());
        	
        	boxStato.getItems().clear();
        	
        	this.boxStato.getItems().addAll(model.getStati());

    	} catch(NullPointerException e) {
    		txtResult.setText("ERRORE: selezionare un anno.\n");
    		return ;
    	}
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }

	public void setModel(Model model) {
		// TODO Auto-generated method stub
		this.model = model ;
		for(AnnoCount ac : model.getAnniAvvistamenti())
			this.boxAnno.getItems().add(ac);
	}
}
