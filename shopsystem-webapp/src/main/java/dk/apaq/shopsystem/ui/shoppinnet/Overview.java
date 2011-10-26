package dk.apaq.shopsystem.ui.shoppinnet;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.ui.shoppinnet.common.PageHeader;

public class Overview extends CustomComponent {
    
    private VerticalLayout pageHolder = new VerticalLayout();
    private HorizontalLayout content = new HorizontalLayout();
    private VerticalLayout contentLeft = new VerticalLayout();
    private VerticalLayout contentRight = new VerticalLayout();
    //private Label spacer = new Label();
    
    
    @Override
    public void attach() {

        setCompositionRoot(this.pageHolder);
        
        // Clear all
        this.pageHolder.removeAllComponents();
        this.content.removeAllComponents();
        this.contentLeft.removeAllComponents();
        this.contentRight.removeAllComponents();
        
        
        this.pageHolder.addComponent(new PageHeader("Overview"));
        this.pageHolder.addComponent(this.content);
        this.pageHolder.setSpacing(true);
        
        this.content.addComponent(this.contentLeft);
        this.content.addComponent(this.contentRight);
        
        this.content.setSpacing(true);
        this.contentLeft.setSpacing(true);
        this.contentRight.setSpacing(true);
        this.content.setWidth("100%");
        this.contentLeft.setWidth("100%");
        this.contentRight.setWidth("100%");
        
        // Left content
        
        Panel box1 = new Panel("Abonnement: Gratis");
        box1.setStyleName("v-box");
        box1.addComponent(new Label("Ved at opgradere til reklamefri, vil din butik fremstå mere professionelt."));
        box1.addComponent(new Button("Køb reklamefri, kun 99 kr."));
        this.contentLeft.addComponent(box1);
        
        Panel box2 = new Panel("Omsætning");
        box2.setStyleName("v-box");
        box2.addComponent(new Label("A description..."));
        this.contentLeft.addComponent(box2);
       
        Panel box4 = new Panel("Nye varer i netværket");
        box4.setStyleName("v-box");
        box4.addComponent(new Label("A description..."));
        this.contentLeft.addComponent(box4);
        
        // Right content

        Panel box5 = new Panel("Hjælp");
        box5.setStyleName("v-box");
        box5.addComponent(new Label("Videoguide, FAQ og/eller Forum"));
        this.contentRight.addComponent(box5);
        
        Panel box6 = new Panel("Hvad er tilladt at sælge?");
        box6.setStyleName("v-box");
        box6.addComponent(new Label("Det er tilladt at sælge alt. Men hvis dine varer ikke kan vurderes som familievenlige, må vi ikke vise reklamer."));
        box6.addComponent(new Label("Det vil derfor kræve at du opgraderer til reklamefri. Bemærk at f.eks. sexlegetøj og erotisk lingeri hører under dette."));
        box6.addComponent(new Label("Er du i tvivl, så opgrader til reklamefri."));
        this.contentRight.addComponent(box6);
        
        Panel box7 = new Panel("Undgå kopiering");
        box7.setStyleName("v-box");
        box7.addComponent(new Label("Undgå at bruge indhold fra andre websites, medmindre du har fået lov. Det er ulovligt, uanset om det er en tekst eller et billede."));
        this.contentRight.addComponent(box7);
        
        /*Panel box7 = new Panel("Varer i butikken med 1 klik");
        box7.setStyleName("v-box");
        box7.addComponent(new Label("Lad vores grossister fylde din butik med varer. Du kan stadig sælge dine egne varer sideløbende."));
        this.contentRight.addComponent(box7);*/
    }
    
}
