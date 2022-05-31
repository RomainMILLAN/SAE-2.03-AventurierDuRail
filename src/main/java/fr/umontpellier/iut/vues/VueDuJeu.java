package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.ICouleurWagon;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.RailsIHM;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Destination;
import fr.umontpellier.iut.rails.Joueur;
import fr.umontpellier.iut.rails.Route;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les 5 cartes Wagons visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    public IJeu jeu;
    //private VuePlateau plateau;
    Button rulesButton;
    VuePlateau plateau;
    private ImageView plateauView;
    private VueJoueurCourant joueurCourant;
    private VBox autresJoueursBox;
    private VueAutresJoueurs autresJoueurs01;
    private VueAutresJoueurs autresJoueurs02;
    private VueAutresJoueurs autresJoueurs03;
    private VueAutresJoueurs autresJoueurs04;

    private Label titlePage;

    private Label instruction;

    //BOTTOM
    BorderPane bas;
    HBox cartesVisibles;
    Button passer;
    HBox listDestinationCard;
    VBox bottomCenter;
    ImageView cardWagonNotVisible;
    ImageView cardDestination;
    VBox bottomLeftCard;
    VBox bottomRight;
    StackPane stackCardDestination;
    StackPane stackCardWagonNotVisible;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        this.setPrefHeight(900);
        this.setPrefWidth(1600);
        this.setMinHeight(900);
        this.setMinWidth(1600);

        this.setStyle("-fx-background-color: #F3DEC4");

        //HAUT
        BorderPane haut = new BorderPane();

        titlePage = new Label("Les Aventuriers du Rail - Europe");
        titlePage.setPrefWidth(750);
        titlePage.setAlignment(Pos.CENTER);
        titlePage.setFont(Font.font("../resources/fonts/Dancing_Script/DancingScript-VariableFont_wght.ttf", FontWeight.NORMAL, 35));
        haut.setCenter(titlePage);
        haut.setAlignment(titlePage, Pos.CENTER);

        //REGLES
        rulesButton = new Button("Règles");
        rulesButton.setText("Règles");
        rulesButton.setPrefWidth(100);
        rulesButton.setPrefHeight(30);
        rulesButton.setTranslateX(10);
        rulesButton.setTranslateY(10);
        rulesButton.setStyle("-fx-background-color: #E8D9C7; -fx-border-radius: 10px; -fx-border-color: #000;");
        rulesButton.setOnMouseClicked(e -> {
            //System.out.println("Règles");
            new RailsIHM().openRules();
        });
        haut.setLeft(rulesButton);

        //Joueur Courant
        joueurCourant = new VueJoueurCourant(this.jeu);

        //Joueur Autre
        autresJoueursBox = new VBox();
        autresJoueurs01 = new VueAutresJoueurs(this.jeu.getJoueurs().get(1));
        if(this.jeu.getJoueurs().size() >= 3){
            autresJoueurs02 = new VueAutresJoueurs(this.jeu.getJoueurs().get(2));
            autresJoueurs02.setTranslateY(75);
        }
        if(this.jeu.getJoueurs().size() >= 4){
            autresJoueurs03 = new VueAutresJoueurs(this.jeu.getJoueurs().get(3));
            autresJoueurs03.setTranslateY(100);
        }
        if(this.jeu.getJoueurs().size() >= 5){
            autresJoueurs04 = new VueAutresJoueurs(this.jeu.getJoueurs().get(4));
            autresJoueurs04.setTranslateY(125);
        }

        System.out.println(this.jeu.getJoueurs().size());
        if(this.jeu.getJoueurs().size() == 2){
            autresJoueursBox.getChildren().addAll(autresJoueurs01);
        }else if(this.jeu.getJoueurs().size() == 3){
            autresJoueursBox.getChildren().addAll(autresJoueurs01, autresJoueurs02);

        }else if(this.jeu.getJoueurs().size() == 4){
            autresJoueursBox.getChildren().addAll(autresJoueurs01, autresJoueurs02, autresJoueurs03);

        }else if(this.jeu.getJoueurs().size() == 5){
            autresJoueursBox.getChildren().addAll(autresJoueurs01, autresJoueurs02, autresJoueurs03, autresJoueurs04);
        }
        //Plateau
        plateau = new VuePlateau();
        plateauView = new ImageView(new Image("images/euMap.jpg"));
        plateauView.setFitHeight(100);
        plateauView.setFitWidth(100);
        plateauView.setTranslateY(0);
        plateauView.setTranslateX(0);

        //Card
        bas = new BorderPane();
        bottomCenter = new VBox();
        bottomCenter.setAlignment(Pos.CENTER);
        //bottomCenter.setTranslateX((this.getPrefWidth()/4)-75);
        instruction = new Label("Recherche de l'instruction en cour : ");
        instruction.setFont(Font.font("Cabin", FontWeight.MEDIUM, 18));
        instruction.setTranslateY(-15);
        bottomCenter.getChildren().add(instruction);
        //Cartes Visibles
        cartesVisibles = new HBox();
        cartesVisibles.setAlignment(Pos.CENTER);
        cartesVisibles.setSpacing(10);

        //Destinations
        listDestinationCard = new HBox();
        listDestinationCard.setAlignment(Pos.CENTER);
        listDestinationCard.setSpacing(7.5);

        bottomCenter.getChildren().addAll(cartesVisibles);
        bottomCenter.getChildren().addAll(listDestinationCard);
        bottomCenter.setTranslateY(-25);
        bas.setCenter(bottomCenter);
        //LEFT - Card
        bottomLeftCard = new VBox();
        stackCardWagonNotVisible = new StackPane();
        for(int i=0; i<5; i++){
            cardWagonNotVisible = new ImageView(new Image("images/wagons.png"));
            cardWagonNotVisible.setFitHeight(75);
            cardWagonNotVisible.setFitWidth(115);
            cardWagonNotVisible.setTranslateY(-50);
            if(i==1){
                cardWagonNotVisible.setRotate(-11.3);
            }else if(i==2){
                cardWagonNotVisible.setRotate(12.5);
            }else if(i==3){
                cardWagonNotVisible.setRotate(10.5);
            }else if(i==4){
                cardWagonNotVisible.setRotate(-2);
            }
            stackCardWagonNotVisible.getChildren().add(cardWagonNotVisible);
        }

        stackCardWagonNotVisible.setOnMouseClicked(e -> {
            this.jeu.uneCarteWagonAEtePiochee();
        });
        stackCardDestination = new StackPane();

        for(int i=0; i<5; i++){
            cardDestination = new ImageView(new Image("images/destinations.png"));
            cardDestination.setFitHeight(75);
            cardDestination.setFitWidth(115);
            cardDestination.setTranslateY(-25);
            if(i==1){
                cardDestination.setRotate(12.5);
            }else if(i==2){
                cardDestination.setRotate(-11.3);
            }else if(i==3){
                cardDestination.setRotate(9.5);
            }else if(i==4){
                cardDestination.setRotate(4);
            }
            stackCardDestination.getChildren().add(cardDestination);
        }

        stackCardDestination.setOnMouseClicked(e -> {
            this.jeu.uneDestinationAEtePiochee();
        });
        bottomLeftCard.getChildren().addAll(stackCardWagonNotVisible, stackCardDestination);
        bottomLeftCard.setTranslateX(25);
        bas.setLeft(bottomLeftCard);
        //Bottom Right
        bottomRight = new VBox();
        //Passer
        passer = new Button("Passer");
        passer.setStyle("-fx-background-color: #F6E7D4; -fx-background-radius: 7.5px; -fx-border-radius: 7.5px;");
        passer.setPrefWidth(250);
        passer.setPrefHeight(40);
        passer.setFont(Font.font("Cabin", FontWeight.MEDIUM, 17.5));
        passer.setTranslateX(-(passer.getPrefWidth()/3));
        passer.setTranslateY(50);
        passer.setOnMouseEntered(e -> {
            passer.setStyle("-fx-background-color: #FFE6C7");
        });
        passer.setOnMouseExited(e -> {
            passer.setStyle("-fx-background-color: #F6E7D4");
        });

        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.5);
        ds.setOffsetY(1.5);
        ds.setRadius(1);
        ds.setColor(Color.BLACK);
        passer.setEffect(ds);

        bottomRight.getChildren().addAll(passer);

        bas.setRight(bottomRight);

        this.setLeft(joueurCourant);
        this.setRight(autresJoueursBox);
        this.setCenter(plateau);
        this.setBottom(bas);
        this.setTop(haut);
    }

    public IJeu getJeu() {
        return jeu;
    }

    public void creerBindings() {
        plateau.creerBindings();
        /**
         * this.heightProperty().addListener(e -> {
         *             System.out.println("toto");
         *         });
         *
         *         this.widthProperty().addListener(e -> {
         *             System.out.println("tata");
         *         });
         */
        /*this.heightProperty().addListener(e -> {
            this.plateauView.setFitHeight(this.getHeight()/1.75);
            //this.joueurCourant.setPrefHeight(this.getHeight()/500);
        });
        this.widthProperty().addListener(e -> {
            this.plateauView.setFitWidth(this.getWidth()/1.75);
            this.joueurCourant.setPrefWidth(this.getWidth()/5);
        });*/
        this.passer.setOnAction(e -> {
            this.jeu.passerAEteChoisi();
        });

        this.jeu.destinationsInitialesProperty().addListener((ListChangeListener<? super Destination>) e -> {
            Platform.runLater(() -> {
                List<? extends Destination> destinationsListTemp = e.getList();

                this.listDestinationCard.getChildren().clear();
                for(int i=0; i<destinationsListTemp.size(); i++){
                    VueDestination vd = new VueDestination(destinationsListTemp.get(i));
                    this.listDestinationCard.getChildren().addAll(vd);

                    vd.getChildren().get(0).setOnMouseClicked(event -> {
                        this.jeu.uneDestinationAEteChoisie(vd.getDestination().getNom());
                    });
                }
            });

        });

        this.jeu.cartesWagonVisiblesProperty().addListener((ListChangeListener<? super CouleurWagon>) e -> {
            Platform.runLater(() -> {
                List<? extends CouleurWagon> cardWagonVisibles = e.getList();

                this.cartesVisibles.getChildren().clear();
                for(int i=0; i<cardWagonVisibles.size(); i++){
                    VueCarteWagon vcw = new VueCarteWagon(cardWagonVisibles.get(i));
                    this.cartesVisibles.getChildren().addAll(vcw);

                    vcw.setOnMouseClicked(event -> {
                        this.jeu.uneCarteWagonAEteChoisie(vcw.getCouleurWagon());
                    });
                }
            });
        });

        this.jeu.joueurCourantProperty().addListener(e -> {
            ArrayList<Joueur> autreJoueurs = new ArrayList<Joueur>();
            for(Joueur j : this.jeu.getJoueurs()){
                if(!j.equals(this.jeu.joueurCourantProperty().get())){
                    autreJoueurs.add(j);
                }
            }

            if(autreJoueurs.size() == 1){
                this.autresJoueurs01.setJoueur(autreJoueurs.get(0));

            }else if(autreJoueurs.size() == 2){
                this.autresJoueurs01.setJoueur(autreJoueurs.get(0));
                this.autresJoueurs02.setJoueur(autreJoueurs.get(1));
            }else if(autreJoueurs.size() == 3){
                this.autresJoueurs01.setJoueur(autreJoueurs.get(0));
                this.autresJoueurs02.setJoueur(autreJoueurs.get(1));
                this.autresJoueurs03.setJoueur(autreJoueurs.get(2));
            }else if(autreJoueurs.size() == 4){
                this.autresJoueurs01.setJoueur(autreJoueurs.get(0));
                this.autresJoueurs02.setJoueur(autreJoueurs.get(1));
                this.autresJoueurs03.setJoueur(autreJoueurs.get(2));
                this.autresJoueurs04.setJoueur(autreJoueurs.get(3));
            }
        });

        this.jeu.instructionProperty().addListener(e -> {
            this.instruction.setText(this.jeu.instructionProperty().getValue());
        });


    }

    public static String convertFrenchColorToEnglishColor(String fc){
        String res = "white";
        if(fc.equalsIgnoreCase("rouge")){
            res = "#FF5C5C";
        }else if(fc.equalsIgnoreCase("vert")){
            res = "#00993D";
        }else if(fc.equalsIgnoreCase("rose")){
            res = "#7941B1";
        }else if(fc.equalsIgnoreCase("bleu")){
            res = "#605CFF";
        }else if(fc.equalsIgnoreCase("jaune")){
            res = "#E4CC09";
        }

        return res;
    }



}