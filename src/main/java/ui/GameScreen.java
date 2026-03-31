package ui;

import game.GameEngine;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Card;
import model.Deck;
import model.GameState;
import model.Song;

import java.io.FileInputStream;
import java.util.stream.Collectors;

public class GameScreen {
    private final MainWindow mainWindow;
    private final GameEngine gameEngine;
    private Deck currentDeck;
    private Scene scene;

    private ImageView cardImageView;
    private Label imagePlaceholderLabel;
    private Label cardNameLabel;
    private Label songTitleLabel;
    private Label songMetaLabel;
    private Label roundLabel;
    private Label deckInfoLabel;
    private Label statusLabel;
    private Label statsLabel;
    private Label queueLabel;
    private ProgressBar playbackProgressBar;
    private Button successButton;
    private Button failureButton;
    private Button prepareButton;

    public GameScreen(MainWindow mainWindow, GameEngine gameEngine) {
        this.mainWindow = mainWindow;
        this.gameEngine = gameEngine;
    }

    public void initialize(Deck deck, int totalRounds) {
        currentDeck = deck;
        createUI();
        setupGameEngine();
        gameEngine.startGame(deck, totalRounds);
        refreshDeckSummary();
    }

    private void createUI() {
        BorderPane root = new BorderPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #f4ede3, #eef4fb);" +
            "-fx-font-family: 'Segoe UI';"
        );

        root.setTop(createTopBar());
        root.setCenter(createCenterArea());
        root.setBottom(createButtonBar());

        scene = new Scene(root, 1280, 820);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(18);
        topBar.setPadding(new Insets(24, 28, 20, 28));
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox titleBlock = new VBox(4);
        Label titleLabel = new Label("Live Round");
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #7a8598;");

        roundLabel = new Label("Round 1 / 10");
        roundLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #1d2a44;");

        deckInfoLabel = new Label("Deck ready");
        deckInfoLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #6d7686;");
        titleBlock.getChildren().addAll(titleLabel, roundLabel, deckInfoLabel);

        VBox statsCard = new VBox(6);
        statsCard.setPadding(new Insets(14, 20, 14, 20));
        statsCard.setStyle(createGlassCardStyle());

        statsLabel = new Label("Success 0 | Failure 0 | Rate 0%");
        statsLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #24324a;");

        queueLabel = new Label("Active cards 0");
        queueLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #6d7686;");
        statsCard.getChildren().addAll(statsLabel, queueLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(titleBlock, spacer, statsCard);
        return topBar;
    }

    private HBox createCenterArea() {
        HBox centerArea = new HBox(24);
        centerArea.setPadding(new Insets(0, 28, 24, 28));

        VBox artworkPanel = new VBox(16);
        artworkPanel.setAlignment(Pos.TOP_CENTER);
        artworkPanel.setPadding(new Insets(24));
        artworkPanel.setPrefWidth(470);
        artworkPanel.setStyle(createPanelStyle("#fffaf3"));

        Label artworkHeader = new Label("Card Artwork");
        artworkHeader.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #7a5c2e;");

        StackPane imageFrame = new StackPane();
        imageFrame.setPadding(new Insets(18));
        imageFrame.setMinHeight(520);
        imageFrame.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffffff, #f7efe4);" +
            "-fx-background-radius: 28;" +
            "-fx-border-radius: 28;" +
            "-fx-border-color: rgba(122,92,46,0.16);"
        );

        cardImageView = new ImageView();
        cardImageView.setFitWidth(360);
        cardImageView.setFitHeight(460);
        cardImageView.setPreserveRatio(true);
        cardImageView.setSmooth(true);

        imagePlaceholderLabel = new Label("Artwork preview area");
        imagePlaceholderLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #8b93a3;");

        imageFrame.getChildren().addAll(cardImageView, imagePlaceholderLabel);

        cardNameLabel = new Label("Ready");
        cardNameLabel.setWrapText(true);
        cardNameLabel.setAlignment(Pos.CENTER);
        cardNameLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #1d2a44;");

        Label artworkHint = new Label("This panel leaves room for larger art, title badges, and extra card metadata.");
        artworkHint.setWrapText(true);
        artworkHint.setAlignment(Pos.CENTER);
        artworkHint.setStyle("-fx-font-size: 12; -fx-text-fill: #7a8598;");

        artworkPanel.getChildren().addAll(artworkHeader, imageFrame, cardNameLabel, artworkHint);

        VBox infoPanel = new VBox(18);
        infoPanel.setPadding(new Insets(24));
        infoPanel.setStyle(createPanelStyle("#f8fbff"));
        HBox.setHgrow(infoPanel, Priority.ALWAYS);

        Label infoHeader = new Label("Now Playing");
        infoHeader.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #4a6589;");

        VBox songCard = new VBox(10);
        songCard.setPadding(new Insets(20));
        songCard.setStyle(createGlassCardStyle());

        songTitleLabel = new Label("Song info pending");
        songTitleLabel.setWrapText(true);
        songTitleLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: #24324a;");

        songMetaLabel = new Label("Song title, file format, and extra info can be shown here.");
        songMetaLabel.setWrapText(true);
        songMetaLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #6d7686;");

        statusLabel = new Label("Select a card and the audio clip will start automatically.");
        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-font-size: 15; -fx-text-fill: #4f5f79;");

        playbackProgressBar = new ProgressBar(0);
        playbackProgressBar.setPrefWidth(Double.MAX_VALUE);
        playbackProgressBar.setStyle(
            "-fx-accent: #2f7d4a;" +
            "-fx-control-inner-background: rgba(47,125,74,0.12);" +
            "-fx-background-radius: 999;"
        );

        songCard.getChildren().addAll(songTitleLabel, songMetaLabel, statusLabel, playbackProgressBar);

        VBox cardDetailCard = new VBox(10);
        cardDetailCard.setPadding(new Insets(20));
        cardDetailCard.setStyle(createGlassCardStyle());

        Label cardDetailTitle = new Label("Current Card Space");
        cardDetailTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #24324a;");

        Label cardDetailHint = new Label("The layout now keeps separate space for art, work name, song title, and future metadata.");
        cardDetailHint.setWrapText(true);
        cardDetailHint.setStyle("-fx-font-size: 13; -fx-text-fill: #6d7686;");

        cardDetailCard.getChildren().addAll(cardDetailTitle, cardDetailHint);

        infoPanel.getChildren().addAll(infoHeader, songCard, cardDetailCard);

        centerArea.getChildren().addAll(artworkPanel, infoPanel);
        return centerArea;
    }

    private VBox createButtonBar() {
        VBox buttonBar = new VBox(15);
        buttonBar.setPadding(new Insets(0, 28, 28, 28));
        buttonBar.setAlignment(Pos.CENTER);

        HBox roundButtonsBox = new HBox(20);
        roundButtonsBox.setAlignment(Pos.CENTER);

        successButton = createButton("Success", "#2f7d4a", e -> submitSuccess());
        failureButton = createButton("Failure", "#c65b48", e -> submitFailure());
        successButton.setDisable(true);
        failureButton.setDisable(true);
        roundButtonsBox.getChildren().addAll(successButton, failureButton);

        prepareButton = createButton("Continue", "#3d6cb5", e -> resumeFromRest());
        prepareButton.setDisable(true);
        prepareButton.setVisible(false);

        Button returnButton = createButton("Back to Menu", "#6f7480", e -> returnToMenu());

        buttonBar.getChildren().addAll(roundButtonsBox, prepareButton, returnButton);
        return buttonBar;
    }

    private Button createButton(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 14 28;" +
            "-fx-border-radius: 14;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;"
        );
        button.setOnAction(handler);
        return button;
    }

    private void setupGameEngine() {
        gameEngine.setGameListener(new GameEngine.GameListener() {
            @Override
            public void onRoundStart(Card card) {
                Platform.runLater(() -> {
                    cardNameLabel.setText(card.getWorkName());
                    songTitleLabel.setText("Song clip queued");
                    songMetaLabel.setText(buildSongList(card));
                    statusLabel.setText("Card selected. Waiting for playback.");
                    updateCardImage(card);
                    prepareButton.setDisable(true);
                    prepareButton.setVisible(false);
                    successButton.setDisable(true);
                    failureButton.setDisable(true);
                    playbackProgressBar.setProgress(0);
                    refreshDeckSummary();
                });
            }

            @Override
            public void onMusicStart(Song song) {
                Platform.runLater(() -> {
                    songTitleLabel.setText(song.getDisplayName());
                    songMetaLabel.setText(String.format("Format %s | File %s",
                        song.getFileFormat().toUpperCase(), song.getFileName()));
                    statusLabel.setText("Clip is playing. Use the art and title area as the main focus.");
                    successButton.setDisable(true);
                    failureButton.setDisable(true);
                });
            }

            @Override
            public void onMusicComplete() {
                Platform.runLater(() -> {
                    statusLabel.setText("Playback finished. Choose the round result.");
                    successButton.setDisable(false);
                    failureButton.setDisable(false);
                    playbackProgressBar.setProgress(1.0);
                });
            }

            @Override
            public void onMusicInterrupted() {
                Platform.runLater(() -> statusLabel.setText("Playback stopped."));
            }

            @Override
            public void onRoundComplete(GameState.Result result) {
                Platform.runLater(() -> {
                    GameState state = gameEngine.getGameState();
                    roundLabel.setText(String.format("Round %d / %d",
                        state.getCurrentRound(), state.getTotalRounds()));
                    updateStats(state);
                    refreshDeckSummary();
                });
            }

            @Override
            public void onRestMusicStart() {
                Platform.runLater(() -> {
                    statusLabel.setText("Rest music is active. Continue when the table is ready.");
                    songTitleLabel.setText("Rest Time");
                    songMetaLabel.setText("Break track is playing.");
                    prepareButton.setDisable(false);
                    prepareButton.setVisible(true);
                });
            }

            @Override
            public void onRestMusicComplete() {
            }

            @Override
            public void onGameOver(GameState state) {
                Platform.runLater(() -> {
                    refreshDeckSummary();
                    showGameOverDialog(state);
                });
            }

            @Override
            public void onError(String error) {
                Platform.runLater(() -> mainWindow.showErrorDialog("Error", error));
            }
        });
    }

    private void submitSuccess() {
        gameEngine.submitRoundResult(GameState.Result.SUCCESS);
    }

    private void submitFailure() {
        gameEngine.submitRoundResult(GameState.Result.FAILURE);
    }

    private void resumeFromRest() {
        gameEngine.resumeFromRest();
    }

    private void updateCardImage(Card card) {
        if (card == null || card.getImageFile() == null || !card.getImageFile().exists()) {
            cardImageView.setImage(null);
            imagePlaceholderLabel.setVisible(true);
            return;
        }

        try (FileInputStream inputStream = new FileInputStream(card.getImageFile())) {
            Image image = new Image(inputStream);
            cardImageView.setImage(image);
            imagePlaceholderLabel.setVisible(false);
        } catch (Exception e) {
            cardImageView.setImage(null);
            imagePlaceholderLabel.setVisible(true);
            System.err.println("Unable to load image: " + e.getMessage());
        }
    }

    private void updateStats(GameState state) {
        int successCount = state.getSuccessCount();
        int failureCount = state.getFailureCount();
        double successRate = state.getSuccessRate();

        statsLabel.setText(String.format(
            "Success %d | Failure %d | Rate %.1f%%",
            successCount, failureCount, successRate
        ));
    }

    private void refreshDeckSummary() {
        GameState state = gameEngine.getGameState();
        Deck deck = state != null ? state.getCurrentDeck() : currentDeck;
        if (deck == null) {
            deckInfoLabel.setText("Deck unavailable");
            queueLabel.setText("Active cards 0");
            return;
        }

        deckInfoLabel.setText(String.format("%s  |  Total cards %d", deck.getDeckName(), deck.getCardCount()));
        queueLabel.setText(String.format("Active cards %d | Inactive cards %d",
            deck.getActiveCardCount(), deck.getInactiveCards().size()));
    }

    private String buildSongList(Card card) {
        if (card == null || card.getSongs().isEmpty()) {
            return "No playable songs linked to this card.";
        }

        return card.getSongs().stream()
            .limit(4)
            .map(Song::getDisplayName)
            .collect(Collectors.joining(" / "));
    }

    private void showGameOverDialog(GameState state) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");

        int successCount = state.getSuccessCount();
        int totalCount = state.getCurrentRound();

        alert.setContentText(String.format(
            "Game finished%n%n" +
            "Rounds: %d%n" +
            "Success: %d%n" +
            "Failure: %d%n" +
            "Rate: %.1f%%",
            totalCount, successCount, totalCount - successCount, state.getSuccessRate()
        ));

        alert.showAndWait();
        returnToMenu();
    }

    private void returnToMenu() {
        gameEngine.abortGame();
        mainWindow.returnToDeckSelection();
    }

    private String createPanelStyle(String color) {
        return "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 24;" +
            "-fx-border-radius: 24;" +
            "-fx-border-color: rgba(29,42,68,0.08);" +
            "-fx-effect: dropshadow(gaussian, rgba(22,33,58,0.10), 24, 0.18, 0, 8);";
    }

    private String createGlassCardStyle() {
        return "-fx-background-color: rgba(255,255,255,0.84);" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-border-color: rgba(36,50,74,0.08);";
    }

    public Scene getScene() {
        return scene;
    }
}
