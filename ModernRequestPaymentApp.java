import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.effect.*;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.scene.effect.*;
import javafx.scene.text.*;

public class ModernRequestPaymentApp extends Application {
    private RequestPaymentApp app;
    private TextField usernameField, passwordField, titleField, amountField, requestIdField;
    private TextArea outputArea;
    private Scene loginScene, mainMenuScene;
    private Stage primaryStage;
    private TextField descriptionField;
    
    private static final String PRIMARY_COLOR = "#D1A684"; // Netflix Red
    private static final String SECONDARY_COLOR = "#564D4D";
    private static final String BACKGROUND_COLOR = "#141414"; // Netflix Dark
    private static final String CARD_COLOR = "#221F1F"; // Netflix Card
    private static final String TEXT_COLOR = "#FFFFFF";
    private static final String HOVER_COLOR = "#831010";
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        app = new RequestPaymentApp();
        
        primaryStage.setTitle("Modern Payment Request");
        createLoginScene();
        createMainMenuScene();
        
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    
    private void createLoginScene() {
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("""
            -fx-background-color: #FFFFFF;
            -fx-background-image: linear-gradient(to bottom, #000000, #141414);
        """);
        mainContainer.setPadding(new Insets(40));

        // Add logo with animation
        Node logo = createLogoNode();
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), logo);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        // App name label
        Label appNameLabel = createStyledLabel("SRM Freelance", 42);
        appNameLabel.setStyle("""
            -fx-text-fill: """ + "#FFFFFF" + """
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, #FFFFFF, 10, 0.5, 0, 0);
            -fx-font-size: 52;
        """);

        // Login card
        VBox loginCard = new VBox(15);
        loginCard.setStyle("""
            -fx-background-color: #221F1F;
            -fx-padding: 30;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, #D1A684, 20, 0, 0, 0);
            -fx-border-color: rgba(255, 255, 255, 0.1);
            -fx-border-radius: 10;
            -fx-border-width: 1;
        """);
        loginCard.setMaxWidth(400);

        // Styled input fields
        usernameField = createStyledTextField("Username");
        passwordField = createStyledPasswordField("Password");
        
        // Add focus animations
        addFocusAnimation(usernameField);
        addFocusAnimation(passwordField);

        // Action buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button loginBtn = createPrimaryButton("Sign In", e -> login());
        Button registerBtn = createSecondaryButton("Sign Up", e -> register());
        
        addButtonAnimation(loginBtn);
        addButtonAnimation(registerBtn);

        buttonBox.getChildren().addAll(loginBtn, registerBtn);
        
        // Status area
        outputArea = createStyledTextArea();
        
        // Add all elements to login card
        loginCard.getChildren().addAll(
            usernameField,
            passwordField,
            buttonBox,
            outputArea
        );

        // Add fade-in animation for the card
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), loginCard);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        
        // Add all components to main container
        mainContainer.getChildren().addAll(logo, appNameLabel, loginCard);
        
        // Create scene
        loginScene = new Scene(mainContainer, 600, 800);
        loginScene.setFill(Color.BLACK);
    }

    private Node createLogoNode() {
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(120);
            logoView.setFitHeight(120);
            logoView.setPreserveRatio(true);
            
            // Add glow effect
            DropShadow glow = new DropShadow();
            glow.setColor(Color.web(PRIMARY_COLOR));
            glow.setRadius(20);
            glow.setSpread(0.4);
            logoView.setEffect(glow);
            
            return logoView;
        } catch (Exception e) {
            System.out.println("Could not load logo image, falling back to default");
            return createDefaultLogo();
        }
    }
    private Node createDefaultLogo() {
        StackPane logoStack = new StackPane();
        
        Circle circle = new Circle(50);
        circle.setFill(Color.web(PRIMARY_COLOR));
        
        Text rupeeSymbol = new Text("₹");
        rupeeSymbol.setFill(Color.WHITE);
        rupeeSymbol.setFont(Font.font("System", FontWeight.BOLD, 60));
        
        logoStack.getChildren().addAll(circle, rupeeSymbol);
        logoStack.setMaxSize(100, 100);
        logoStack.setMinSize(100, 100);
        
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(PRIMARY_COLOR));
        glow.setRadius(20);
        glow.setSpread(0.4);
        logoStack.setEffect(glow);
        
        return logoStack;
    }

    private Node createBanner() {
        // Debug print the image path
        System.out.println("Attempting to load banner image...");
        String imagePath = "/images/banner.png";
        System.out.println("Image path: " + imagePath);
        
        try {
            // Debug print the resource stream
            var resourceStream = getClass().getResourceAsStream(imagePath);
            if (resourceStream == null) {
                System.out.println("Resource stream is null! Image not found at: " + imagePath);
                System.out.println("Make sure banner.png is in: src/main/resources/images/");
                return createDefaultBanner();
            }
            
            System.out.println("Resource stream found successfully!");
            Image bannerImage = new Image(resourceStream);
            
            // Debug print image loading
            if (bannerImage.isError()) {
                System.out.println("Error loading image: " + bannerImage.getException());
                return createDefaultBanner();
            }
            
            System.out.println("Banner image loaded successfully!");
            System.out.println("Image dimensions: " + bannerImage.getWidth() + "x" + bannerImage.getHeight());
            
            ImageView bannerView = new ImageView(bannerImage);
            bannerView.setFitWidth(600);
            bannerView.setFitHeight(120);
            bannerView.setPreserveRatio(false);
            
            // Create banner content
            HBox bannerContent = new HBox();
            bannerContent.setAlignment(Pos.CENTER_RIGHT);
            bannerContent.setPadding(new Insets(10, 20, 10, 20));
            bannerContent.setSpacing(10);

            Label userLabel = new Label();
            userLabel.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 16;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(gaussian, black, 2, 0, 0, 0);
            """);
            userLabel.textProperty().bind(app.currentUserProperty());

            Button logoutBtn = createIconButton("logout.png", e -> logout());

            bannerContent.getChildren().addAll(userLabel, logoutBtn);
            
            // Stack banner image and content
            StackPane bannerStack = new StackPane();
            bannerStack.getChildren().addAll(bannerView, bannerContent);
            
            return bannerStack;
            
        } catch (Exception e) {
            System.out.println("Exception loading banner image:");
            e.printStackTrace();
            return createDefaultBanner();
        }
    }
    private Node createDefaultBanner() {
        Rectangle banner = new Rectangle();
        banner.setWidth(600);
        banner.setHeight(120);
        
        // Create gradient background
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web(PRIMARY_COLOR)),
            new Stop(1, Color.web(SECONDARY_COLOR))
        );
        banner.setFill(gradient);
        
        // Create banner content
        HBox bannerContent = new HBox();
        bannerContent.setAlignment(Pos.CENTER_RIGHT);
        bannerContent.setPadding(new Insets(10, 20, 10, 20));
        bannerContent.setSpacing(10);

        Label userLabel = new Label();
        userLabel.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 16;
            -fx-font-weight: bold;
            -fx-effect: dropshadow(gaussian, black, 2, 0, 0, 0);
        """);
        userLabel.textProperty().bind(app.currentUserProperty());

        Button logoutBtn = createIconButton("logout.png", e -> logout());

        bannerContent.getChildren().addAll(userLabel, logoutBtn);
        
        // Stack banner and content
        StackPane bannerStack = new StackPane(banner, bannerContent);
        
        return bannerStack;
    }
    
private void createMainMenuScene() {
    BorderPane mainContainer = new BorderPane();
    mainContainer.setStyle("""
        -fx-background-color: #000000;
        -fx-background-image: linear-gradient(to bottom, #000000, #141414);
    """);
    Node banner = createBanner();
            mainContainer.setTop(banner);

    HBox bannerContent = new HBox();
    bannerContent.setAlignment(Pos.CENTER_RIGHT);
    bannerContent.setPadding(new Insets(10, 20, 10, 20));
    bannerContent.setSpacing(10);

    Label userLabel = new Label();
    userLabel.setStyle("""
        -fx-text-fill: white;
        -fx-font-size: 16;
    """);
    userLabel.textProperty().bind(app.currentUserProperty());

    Button logoutBtn = createIconButton("logout.png", e -> logout());

    bannerContent.getChildren().addAll(userLabel, logoutBtn);
    
    // Stack banner and content
    StackPane bannerStack = new StackPane(banner, bannerContent);
    bannerStack.setAlignment(Pos.CENTER_RIGHT);
    mainContainer.setTop(bannerStack);
HBox topBanner = createTopBanner();
        mainContainer.setTop(topBanner);    
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setStyle("""
        -fx-background: #141414;
        -fx-background-color: #141414;
        -fx-control-inner-background: #141414;
        -fx-padding: 0;
    """);

    VBox cardsContainer = new VBox(20);
    cardsContainer.setStyle("-fx-background-color: #141414;");
    cardsContainer.setPadding(new Insets(20));

    // Cards with hover animations
    VBox balanceCard = createDashboardCard("Current Balance", 
        createBalanceView(), 
        createPrimaryButton("Check Balance", e -> checkBalance()));
    
    VBox requestCard = createDashboardCard("Payment Requests",
        createActionButtonsGrid(),
        null);
        
    VBox activityCard = createDashboardCard("Recent Activity",
        outputArea = createStyledTextArea(),
        null);

    // Add hover effects to cards
    addCardHoverEffect(balanceCard);
    addCardHoverEffect(requestCard);
    addCardHoverEffect(activityCard);

    cardsContainer.getChildren().addAll(balanceCard, requestCard, activityCard);
    
    // Animate cards on load
    for (Node card : cardsContainer.getChildren()) {
        card.setOpacity(0);
        card.setTranslateY(50);
    }

    scrollPane.setContent(cardsContainer);
    mainContainer.setCenter(scrollPane);

    mainMenuScene = new Scene(mainContainer, 600, 800);
    mainMenuScene.setFill(Color.BLACK);

    // Sequence the card animations
    Timeline timeline = new Timeline();
    double delay = 0;
    for (Node card : cardsContainer.getChildren()) {
        KeyFrame kf1 = new KeyFrame(Duration.seconds(delay),
            new KeyValue(card.opacityProperty(), 1),
            new KeyValue(card.translateYProperty(), 0, Interpolator.EASE_OUT)
        );
        timeline.getKeyFrames().add(kf1);
        delay += 0.2;
    }
    timeline.play();
}
private void addCardHoverEffect(Node card) {
    card.setOnMouseEntered(e -> {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
        st.setToX(1.03);
        st.setToY(1.03);
        st.play();
    });

    card.setOnMouseExited(e -> {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
        st.setToX(1);
        st.setToY(1);
        st.play();
    });
}

private void addButtonAnimation(Button button) {
    button.setOnMouseEntered(e -> {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setToX(1.05);
        st.setToY(1.05);
        st.play();
    });

    button.setOnMouseExited(e -> {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setToX(1);
        st.setToY(1);
        st.play();
    });
}

private void addFocusAnimation(TextField field) {
    field.focusedProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal) {
            new Timeline(
                new KeyFrame(Duration.millis(100),
                    new KeyValue(field.scaleXProperty(), 1.02),
                    new KeyValue(field.scaleYProperty(), 1.02)
                )
            ).play();
        } else {
            new Timeline(
                new KeyFrame(Duration.millis(100),
                    new KeyValue(field.scaleXProperty(), 1),
                    new KeyValue(field.scaleYProperty(), 1)
                )
            ).play();
        }
    });
}
    
private HBox createTopBanner() {
    HBox banner = new HBox();
    banner.setStyle("""
        -fx-background-color: #221F1F;
        -fx-padding: 15 20;
        -fx-spacing: 10;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 4);
    """);
    banner.setAlignment(Pos.CENTER_RIGHT);
    
    Label userLabel = new Label();
    userLabel.setStyle("""
        -fx-text-fill: white;
        -fx-font-size: 16;
    """);
    userLabel.textProperty().bind(app.currentUserProperty());
    
    Button logoutBtn = createIconButton("logout.png", e -> logout());
    
    banner.getChildren().addAll(userLabel, logoutBtn);
    return banner;
}
private void showChatRequestDialog() {
    Dialog<String> dialog = createStyledInputDialog(
        "Open Chat",
        "Enter the ID of the request you want to chat about:",
        "Request ID"
    );
    
    dialog.showAndWait().ifPresent(result -> {
        try {
            int requestId = Integer.parseInt(result);
            showChatDialog(requestId);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Input", "Please enter a valid request ID.");
        }
    });
}
private void updateChatArea(TextArea chatArea, int requestId) {
        // Debug print
        System.out.println("Updating chat area for request: " + requestId);
        
        List<Message> messages = app.getChatMessages(requestId);
        System.out.println("Retrieved " + messages.size() + " messages");
        
        StringBuilder chat = new StringBuilder();
        
        for (Message msg : messages) {
            String formattedMessage = String.format("[%s] %s: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                msg.getSender(),
                msg.getContent());
            chat.append(formattedMessage);
            System.out.println("Adding message to display: " + formattedMessage.trim());
        }
        
        // Always update the text area
        chatArea.setText(chat.toString());
        Platform.runLater(() -> chatArea.setScrollTop(Double.MAX_VALUE));
    }

    private void showChatDialog(int requestId) {
        Dialog<ButtonType> dialog = createStyledDialog("Chat - Request #" + requestId);
        dialog.setResizable(true);
        
        VBox chatContainer = new VBox(10);
        chatContainer.setPadding(new Insets(10));
        chatContainer.setPrefWidth(400);
        chatContainer.setPrefHeight(500);
        
        TextArea chatArea = createStyledTextArea();
        chatArea.setPrefHeight(400);
        chatArea.setWrapText(true);
        
        TextField messageInput = createStyledTextField("Type a message...");
        
        Button sendButton = createPrimaryButton("Send", e -> {
            String message = messageInput.getText().trim();
            if (!message.isEmpty()) {
                app.sendMessage(requestId, message);
                messageInput.clear();
                updateChatArea(chatArea, requestId);
            }
        });
        
        messageInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && !messageInput.getText().trim().isEmpty()) {
                sendButton.fire();
            }
        });
        
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(messageInput, sendButton);
        
        chatContainer.getChildren().addAll(chatArea, inputBox);
        
        Timeline autoUpdate = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> updateChatArea(chatArea, requestId))
        );
        autoUpdate.setCycleCount(Animation.INDEFINITE);
        autoUpdate.play();
        
        dialog.setOnCloseRequest(e -> autoUpdate.stop());
        dialog.getDialogPane().setContent(chatContainer);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        updateChatArea(chatArea, requestId);
        styleDialogButtons(dialog);
        dialog.showAndWait();
    }
    
    private GridPane createActionButtonsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        
        // Add buttons in a 2x3 grid
        grid.add(createActionButton("Create Request", "create.png", e -> showCreateRequestDialog()), 0, 0);
        grid.add(createActionButton("Accept Request", "accept.png", e -> showAcceptRequestDialog()), 1, 0);
        grid.add(createActionButton("Cancel Request", "cancel.png", e -> showCancelRequestDialog()), 0, 1);
        grid.add(createActionButton("Complete Request", "complete.png", e -> showCompleteRequestDialog()), 1, 1);
        grid.add(createActionButton("List Requests", "list.png", e -> listOpenRequests()), 0, 2);
        grid.add(createActionButton("Chat", "chat.png", e -> showChatRequestDialog()), 1, 2);

        // Center the grid
        StackPane centeredGrid = new StackPane(grid);
        centeredGrid.setPadding(new Insets(20));
        
        return grid;
    }
    
    

    private VBox createDashboardCard(String title, Node content, Node action) {
        VBox card = new VBox(15);
        card.setStyle("""
            -fx-background-color: #221F1F;
            -fx-padding: 20;
            -fx-background-radius: 8;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 0);
            -fx-border-color: rgba(255, 255, 255, 0.1);
            -fx-border-radius: 8;
            -fx-border-width: 1;
        """);
        
        Label titleLabel = createStyledLabel(title, 18);
        card.getChildren().add(titleLabel);
        
        if (content != null) card.getChildren().add(content);
        if (action != null) card.getChildren().add(action);
        
        return card;
    }

    
    private VBox createBalanceView() {
        VBox balanceView = new VBox(5);
        balanceView.setAlignment(Pos.CENTER);
        
        Label balanceLabel = new Label("₹0.00");
        balanceLabel.setStyle("""
            -fx-font-size: 36;
            -fx-font-weight: bold;
            -fx-text-fill: white;
        """);
        
        balanceView.getChildren().add(balanceLabel);
        return balanceView;
    }
    
    // Utility methods for creating styled components
    private Button createPrimaryButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setStyle("""
            -fx-background-color: """ + PRIMARY_COLOR + """
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-padding: 12 24;
            -fx-background-radius: 4;
            -fx-cursor: hand;
        """);
        
        // Hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle("""
                -fx-background-color: #831010;
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-font-weight: bold;
                -fx-padding: 12 24;
                -fx-background-radius: 4;
                -fx-cursor: hand;
            """)
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle("""
                -fx-background-color: """ + PRIMARY_COLOR + """
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-font-weight: bold;
                -fx-padding: 12 24;
                -fx-background-radius: 4;
                -fx-cursor: hand;
            """)
        );
        
        button.setOnAction(action);
        return button;
    }
    
    private Button createSecondaryButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setStyle("""
            -fx-background-color: rgba(51, 51, 51, 0.8);
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-padding: 12 24;
            -fx-background-radius: 4;
            -fx-cursor: hand;
        """);
        
        // Hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle("""
                -fx-background-color: rgba(77, 77, 77, 0.8);
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-font-weight: bold;
                -fx-padding: 12 24;
                -fx-background-radius: 4;
                -fx-cursor: hand;
            """)
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle("""
                -fx-background-color: rgba(51, 51, 51, 0.8);
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-font-weight: bold;
                -fx-padding: 12 24;
                -fx-background-radius: 4;
                -fx-cursor: hand;
            """)
        );
        
        button.setOnAction(action);
        return button;
    }
    
    private Button createActionButton(String text, String iconPath, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        VBox buttonContent = new VBox(10);
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setPrefWidth(150);  // Fixed width for all buttons
        
        // Create circular background for icon
        Circle iconBackground = new Circle(25);
        iconBackground.setFill(Color.web(PRIMARY_COLOR));
        
        // Create icon text
        Text icon = new Text();
        icon.setFill(Color.WHITE);
        icon.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        // Set icon based on button type
        switch(text.toLowerCase()) {
            case "create request" -> icon.setText("+");
            case "accept request" -> icon.setText("✓");
            case "cancel request" -> icon.setText("✕");
            case "complete request" -> icon.setText("★");
            case "list requests" -> icon.setText("☰");
            case "chat" -> icon.setText("✉");
            default -> icon.setText("•");
        }
        
        // Stack icon on background
        StackPane iconStack = new StackPane(iconBackground, icon);
        
        // Button label
        Label label = new Label(text);
        label.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-wrap-text: true;
            -fx-text-alignment: center;
        """);
        
        buttonContent.getChildren().addAll(iconStack, label);
        
        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setPrefWidth(180);  // Fixed width
        button.setPrefHeight(120); // Fixed height
        button.setStyle("""
            -fx-background-color: rgba(51, 51, 51, 0.8);
            -fx-background-radius: 10;
            -fx-cursor: hand;
            -fx-padding: 15;
        """);
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle("""
                -fx-background-color: rgba(77, 77, 77, 0.8);
                -fx-background-radius: 10;
                -fx-cursor: hand;
                -fx-padding: 15;
            """);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("""
                -fx-background-color: rgba(51, 51, 51, 0.8);
                -fx-background-radius: 10;
                -fx-cursor: hand;
                -fx-padding: 15;
            """);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        
        button.setOnAction(action);
        return button;
    }

    
    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("""
            -fx-background-color: #333333;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #808080;
            -fx-background-radius: 4;
            -fx-padding: 12;
            -fx-font-size: 14;
            -fx-highlight-fill: """ + PRIMARY_COLOR + ";");
        return field;
    }
    
    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle("""
            -fx-background-color: #333333;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #808080;
            -fx-background-radius: 4;
            -fx-padding: 12;
            -fx-font-size: 14;
            -fx-highlight-fill: """ + PRIMARY_COLOR + ";");
        return field;
    }
    
    private TextArea createStyledTextArea() {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setWrapText(true);
        area.setStyle("""
            -fx-background-color: #333333;
            -fx-text-fill: white;
            -fx-background-radius: 4;
            -fx-padding: 10;
            -fx-font-size: 14;
            -fx-control-inner-background: #262626;
            -fx-highlight-fill: """ + PRIMARY_COLOR + ";");
        return area;
    }

    private Label createStyledLabel(String text, double fontSize) {
        Label label = new Label(text);
        label.setStyle("""
            -fx-font-size: """ + fontSize + """
            -fx-font-weight: bold;
            -fx-text-fill: white;
        """);
        return label;
    }
    
    private ImageView createImageView(String path, double width, double height) {
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(path)));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
    
    private Button createIconButton(String iconPath, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        ImageView icon = createImageView("/images/" + iconPath, 24, 24);
        Button button = new Button();
        button.setGraphic(icon);
        button.setStyle("""
            -fx-background-color: transparent;
            -fx-cursor: hand;
        """);
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle("""
                -fx-background-color: rgba(77, 77, 77, 0.8);
                -fx-cursor: hand;
            """);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("""
                -fx-background-color: transparent;
                -fx-cursor: hand;
            """);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        
        button.setOnAction(action);
        return button;
    }

    
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean loginSuccess = app.login(username, password);
        if (loginSuccess) {
            showSuccessDialog("Login successful!");
            primaryStage.setScene(mainMenuScene);
        } else {
            showErrorDialog("Login failed", "Please check your credentials and try again.");
        }
        passwordField.clear();
    }

    private void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Registration Error", "Username and password cannot be empty.");
            return;
        }
        
        boolean success = app.registerUser(username, password, 1000.0); // 1000 INR initial balance
        if (success) {
            showSuccessDialog("Registration successful!\nInitial balance: ₹1000");
        } else {
            showErrorDialog("Registration Failed", "Username might already exist.");
        }
        passwordField.clear();
    }

    private void logout() {
        app.logout();
        outputArea.clear();
        usernameField.clear();
        passwordField.clear();
        primaryStage.setScene(loginScene);
    }

    private void showCreateRequestDialog() {
        Dialog<ButtonType> dialog = createStyledDialog("Create Payment Request");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        
        titleField = createStyledTextField("Request Title");
        amountField = createStyledTextField("Amount");
        descriptionField = createStyledTextField("Description");
        
        grid.add(new Label("Title"), 0, 0);
        grid.add(titleField, 0, 1);
        grid.add(new Label("Amount (₹)"), 0, 2);
        grid.add(amountField, 0, 3);
        grid.add(new Label("Description"), 0, 4);
        grid.add(descriptionField, 0, 5);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
        
        styleDialogButtons(dialog);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButton) {
                createRequest();
            }
            return null;
        });
        
        dialog.showAndWait();
    }

    private void createRequest() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        
        if (title.isEmpty() || description.isEmpty()) {
            showErrorDialog("Invalid Input", "Please fill in all fields.");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showErrorDialog("Invalid Amount", "Amount must be greater than 0.");
                return;
            }
            
            app.createRequest(title, description, amount);
            showSuccessDialog("Request created successfully!\nTitle: " + title + "\nAmount: ₹" + amount);
            updateActivityFeed("Created new request: " + title + " for ₹" + amount);
            
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Amount", "Please enter a valid number.");
        }
    }

    private void listOpenRequests() {
        String requests = app.listOpenRequests();
        if (requests.isEmpty()) {
            showInfoDialog("Open Requests", "No open requests found.");
        } else {
            TextArea requestsArea = createStyledTextArea();
            requestsArea.setText(requests);
            
            Dialog<ButtonType> dialog = createStyledDialog("Open Requests");
            dialog.getDialogPane().setContent(requestsArea);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            styleDialogButtons(dialog);
            
            dialog.showAndWait();
        }
        updateActivityFeed("Viewed open requests");
    }

    private void showCancelRequestDialog() {
        Dialog<String> dialog = createStyledInputDialog(
            "Cancel Request",
            "Enter the ID of the request you want to cancel:",
            "Request ID"
        );
        
        dialog.showAndWait().ifPresent(result -> {
            try {
                int requestId = Integer.parseInt(result);
                cancelRequest(requestId);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Please enter a valid request ID.");
            }
        });
    }

    private void cancelRequest(int requestId) {
        boolean success = app.cancelRequest(requestId);
        if (success) {
            showSuccessDialog("Request cancelled successfully!\nID: " + requestId);
            updateActivityFeed("Cancelled request #" + requestId);
        } else {
            showErrorDialog("Cancellation Failed", 
                "Could not cancel request #" + requestId + ".\nIt may have already been accepted.");
        }
    }

    private void showAcceptRequestDialog() {
        Dialog<String> dialog = createStyledInputDialog(
            "Accept Request",
            "Enter the ID of the request you want to accept:",
            "Request ID"
        );
        
        dialog.showAndWait().ifPresent(result -> {
            try {
                int requestId = Integer.parseInt(result);
                acceptRequest(requestId);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Please enter a valid request ID.");
            }
        });
    }

    private void acceptRequest(int requestId) {
        try {
            app.acceptRequest(requestId);
            showSuccessDialog("Request accepted successfully!\nID: " + requestId);
            updateActivityFeed("Accepted request #" + requestId);
        } catch (Exception e) {
            showErrorDialog("Accept Failed", 
                "Could not accept request #" + requestId + ".\n" + e.getMessage());
        }
    }

    private void showCompleteRequestDialog() {
        Dialog<String> dialog = createStyledInputDialog(
            "Complete Request",
            "Enter the ID of the request you want to complete:",
            "Request ID"
        );
        
        dialog.showAndWait().ifPresent(result -> {
            try {
                int requestId = Integer.parseInt(result);
                completeRequest(requestId);
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Please enter a valid request ID.");
            }
        });
    }

    private void completeRequest(int requestId) {
        try {
            app.completeRequest(requestId);
            showSuccessDialog("Request completed successfully!\nID: " + requestId);
            updateActivityFeed("Completed request #" + requestId);
        } catch (Exception e) {
            showErrorDialog("Completion Failed", 
                "Could not complete request #" + requestId + ".\n" + e.getMessage());
        }
    }

    private void checkBalance() {
        double balance = app.getUserBalance();
        showInfoDialog("Current Balance", 
            String.format("Your current balance is: ₹%.2f", balance));
        updateActivityFeed("Checked balance: ₹" + String.format("%.2f", balance));
    }

    // Utility methods for dialogs and notifications
    private Dialog<ButtonType> createStyledDialog(String title) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("""
            -fx-background-color: #221F1F;
            -fx-padding: 20;
            -fx-text-fill: white;
        """);
        // Force the dark theme on the dialog's scene
        dialogPane.getScene().setFill(Color.web("#221F1F"));
        return dialog;
    }

    private Dialog<String> createStyledInputDialog(String title, String header, String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(prompt);
        
        dialog.getDialogPane().setStyle("""
            -fx-background-color: """ + CARD_COLOR + """
            -fx-padding: 20;
        """);
        
        styleDialogButtons(dialog);
        
        TextField input = dialog.getEditor();
        input.setStyle("""
            -fx-background-color: #F5F5F5;
            -fx-background-radius: 5;
            -fx-padding: 10;
            -fx-font-size: 14;
        """);
        
        return dialog;
    }

    private void styleDialogButtons(Dialog<?> dialog) {
        dialog.getDialogPane().getButtonTypes().stream()
            .map(buttonType -> (Button) dialog.getDialogPane().lookupButton(buttonType))
            .forEach(button -> {
                if (button.getText().equals("OK") || 
                    button.getText().equals("Create") || 
                    button.getText().equals("Accept")) {
                    button.setStyle("""
                        -fx-background-color: """ + PRIMARY_COLOR + """
                        -fx-text-fill: white;
                        -fx-font-size: 14;
                        -fx-font-weight: bold;
                        -fx-padding: 10 20;
                        -fx-background-radius: 4;
                        -fx-cursor: hand;
                    """);
                    
                    button.setOnMouseEntered(e -> 
                        button.setStyle("""
                            -fx-background-color: #831010;
                            -fx-text-fill: white;
                            -fx-font-size: 14;
                            -fx-font-weight: bold;
                            -fx-padding: 10 20;
                            -fx-background-radius: 4;
                            -fx-cursor: hand;
                        """)
                    );
                    
                    button.setOnMouseExited(e -> 
                        button.setStyle("""
                            -fx-background-color: """ + PRIMARY_COLOR + """
                            -fx-text-fill: white;
                            -fx-font-size: 14;
                            -fx-font-weight: bold;
                            -fx-padding: 10 20;
                            -fx-background-radius: 4;
                            -fx-cursor: hand;
                        """)
                    );
                } else {
                    button.setStyle("""
                        -fx-background-color: rgba(51, 51, 51, 0.8);
                        -fx-text-fill: white;
                        -fx-font-size: 14;
                        -fx-font-weight: bold;
                        -fx-padding: 10 20;
                        -fx-background-radius: 4;
                        -fx-cursor: hand;
                    """);
                    
                    button.setOnMouseEntered(e -> 
                        button.setStyle("""
                            -fx-background-color: rgba(77, 77, 77, 0.8);
                            -fx-text-fill: white;
                            -fx-font-size: 14;
                            -fx-font-weight: bold;
                            -fx-padding: 10 20;
                            -fx-background-radius: 4;
                            -fx-cursor: hand;
                        """)
                    );
                    
                    button.setOnMouseExited(e -> 
                        button.setStyle("""
                            -fx-background-color: rgba(51, 51, 51, 0.8);
                            -fx-text-fill: white;
                            -fx-font-size: 14;
                            -fx-font-weight: bold;
                            -fx-padding: 10 20;
                            -fx-background-radius: 4;
                            -fx-cursor: hand;
                        """)
                    );
                }
            });
    }

    private void showSuccessDialog(String message) {
        Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, "Success", message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = createStyledAlert(Alert.AlertType.ERROR, title, message);
        alert.showAndWait();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = createStyledAlert(Alert.AlertType.INFORMATION, title, message);
        alert.showAndWait();
    }

    private Alert createStyledAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("""
            -fx-background-color: #221F1F;
            -fx-text-fill: white;
            -fx-padding: 20;
        """);
        
        // Make sure all text in the alert is visible
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");
        dialogPane.getScene().setFill(Color.web("#221F1F"));
        
        styleDialogButtons(alert);
        return alert;
    }

    private void updateActivityFeed(String activity) {
        String timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        outputArea.appendText(timestamp + " - " + activity + "\n");
    }    
    public static void main(String[] args) {
        launch(args);
    }
}