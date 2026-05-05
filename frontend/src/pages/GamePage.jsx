import { useState, useEffect, useRef } from "react";
import { startGame, getGameState } from "../services/api";
import { connectGame, connectChat, sendAction, sendChatMessage } from "../services/websocket";
import CardComponent from "../components/Card";
import ColorPicker from "../components/ColorPicker";
import Chat from "../components/Chat";

export default function GamePage({ gameId, player, onLeave }) {
    const [gameState, setGameState] = useState(null);
    const [started, setStarted] = useState(false);
    const [showColorPicker, setShowColorPicker] = useState(false);
    const [pendingCardId, setPendingCardId] = useState(null);
    const [messages, setMessages] = useState([]);
    const [error, setError] = useState("");
    const wsGame = useRef(null);
    const wsChat = useRef(null);

    useEffect(() => {
        wsGame.current = connectGame(gameId, player.id, (state) => {
            // si c'est une erreur
            if (state.error) {
                setError(state.error);
                return;
            }
            setGameState(state);
            if (state.status === "IN_PROGRESS" || state.status === "FINISHED") {
                setStarted(true);
            }
        });

        wsChat.current = connectChat(gameId, player.id, (msg) => {
            setMessages(prev => [...prev, msg]);
        });

        getGameState(gameId, player.id).then(state => {
            setGameState(state);
            if (state.status === "IN_PROGRESS") setStarted(true);
        });

        return () => {
            wsGame.current?.close();
            wsChat.current?.close();
        };
    }, []);

    const handleStart = async () => {
        await startGame(gameId);
        const state = await getGameState(gameId, player.id);
        setGameState(state);
        setStarted(true);
    };

    const handlePlayCard = (cardId, value) => {
        setError("");
        if (value === "8") {
            setPendingCardId(cardId);
            setShowColorPicker(true);
        } else {
            sendAction(wsGame.current, "play", cardId, null);
        }
    };

    const handleColorChosen = (color) => {
        sendAction(wsGame.current, "play", pendingCardId, color);
        setShowColorPicker(false);
        setPendingCardId(null);
    };

    const handleDraw = () => {
        setError("");
        sendAction(wsGame.current, "draw");
    };

    const handleSendMessage = (content) => {
        sendChatMessage(wsChat.current, content);
    };

    const isMyTurn = gameState?.currentPlayer?.username === player.username;

    return (
        <div style={styles.container}>
            {/* HEADER */}
            <div style={styles.header}>
                <div style={styles.logo}>
                    <span style={{ color: "#e8e8f0" }}>BLAZING</span>
                    <span style={{ color: "#e63946" }}>8s</span>
                </div>
                <div style={styles.headerCenter}>
                    <span style={styles.gameIdLabel}>Partie #{gameId}</span>
                    {gameState && (
                        <span style={{
                            ...styles.turnBadge,
                            background: isMyTurn ? "rgba(45,198,83,0.15)" : "rgba(136,136,170,0.1)",
                            color: isMyTurn ? "#2dc653" : "#8888aa",
                        }}>
                            {isMyTurn ? "⚡ Votre tour" : `Tour de ${gameState.currentPlayer?.username}`}
                        </span>
                    )}
                </div>
                <button className="btn btn-secondary" onClick={onLeave}>
                    Quitter
                </button>
            </div>

            <div style={styles.main}>
                {/* ZONE DE JEU */}
                <div style={styles.gameArea}>

                    {/* FIN DE PARTIE */}
                    {gameState?.status === "FINISHED" && (
                        <div style={styles.winBanner}>
                            🎉 Partie terminée !
                        </div>
                    )}

                    {/* ADVERSAIRES */}
                    {gameState?.opponents && (
                        <div style={styles.opponents}>
                            {gameState.opponents.map((op, i) => (
                                <div key={i} style={styles.opponentCard}>
                                    <span style={styles.opponentName}>{op.username}</span>
                                    <span style={styles.opponentCards}>
                                        🃏 {op.handSize} cartes
                                    </span>
                                </div>
                            ))}
                        </div>
                    )}

                    {/* TABLE CENTRALE */}
                    <div style={styles.table}>
                        {/* DEFAUSSE */}
                        <div style={styles.pileContainer}>
                            <p style={styles.pileLabel}>Défausse</p>
                            {gameState?.topCard ? (
                                <CardComponent card={gameState.topCard} disabled />
                            ) : (
                                <div style={styles.emptyPile}>?</div>
                            )}
                        </div>

                        {/* COULEUR ACTIVE */}
                        {gameState?.currentColor && (
                            <div style={styles.colorIndicator}>
                                <p style={styles.pileLabel}>Couleur active</p>
                                <div style={{
                                    ...styles.colorDot,
                                    background: colorMap[gameState.currentColor],
                                    boxShadow: `0 0 20px ${colorMap[gameState.currentColor]}80`
                                }} />
                                <span style={{ color: "#8888aa", fontSize: "0.85rem" }}>
                                    {gameState.currentColor}
                                </span>
                            </div>
                        )}

                        {/* PIOCHE */}
                        <div style={styles.pileContainer}>
                            <p style={styles.pileLabel}>Pioche</p>
                            <div
                                style={{
                                    ...styles.drawPile,
                                    cursor: isMyTurn && started ? "pointer" : "default",
                                    opacity: isMyTurn && started ? 1 : 0.5,
                                }}
                                onClick={isMyTurn && started ? handleDraw : undefined}
                            >
                                🃏
                            </div>
                        </div>
                    </div>

                    {/* BOUTON DEMARRER */}
                    {!started && (
                        <button
                            className="btn btn-primary"
                            onClick={handleStart}
                            style={{ margin: "20px auto", display: "block" }}
                        >
                            Démarrer la partie
                        </button>
                    )}

                    {/* ERREUR */}
                    {error && <p style={styles.error}>{error}</p>}

                    {/* MAIN DU JOUEUR */}
                    {gameState?.hand && (
                        <div>
                            <p style={styles.handLabel}>
                                Votre main ({gameState.hand.length} cartes)
                            </p>
                            <div style={styles.hand}>
                                {gameState.hand.map(card => (
                                    <CardComponent
                                        key={card.id}
                                        card={card}
                                        onClick={() => isMyTurn && started
                                            ? handlePlayCard(card.id, card.value)
                                            : null
                                        }
                                        disabled={!isMyTurn || !started}
                                    />
                                ))}
                            </div>
                        </div>
                    )}
                </div>

                {/* CHAT */}
                <Chat messages={messages} onSend={handleSendMessage} />
            </div>

            {/* COLOR PICKER */}
            {showColorPicker && (
                <ColorPicker
                    onChoose={handleColorChosen}
                    onClose={() => setShowColorPicker(false)}
                />
            )}
        </div>
    );
}

const colorMap = {
    ROUGE: "#e63946",
    BLEU: "#4361ee",
    VERT: "#2dc653",
    JAUNE: "#f4a261",
    NONE: "#8888aa",
};

const styles = {
    container: { minHeight: "100vh", background: "#0a0a0f", display: "flex", flexDirection: "column" },
    header: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "16px 32px",
        borderBottom: "1px solid #2a2a3d",
        background: "#12121a",
    },
    logo: { fontSize: "1.4rem", fontFamily: "'Exo 2', sans-serif", fontWeight: 900 },
    headerCenter: { display: "flex", flexDirection: "column", alignItems: "center", gap: 6 },
    gameIdLabel: { color: "#8888aa", fontSize: "0.8rem", letterSpacing: "0.1em" },
    turnBadge: {
        padding: "4px 16px",
        borderRadius: 20,
        fontSize: "0.85rem",
        fontWeight: 700,
        letterSpacing: "0.05em",
        fontFamily: "'Exo 2', sans-serif",
    },
    main: { display: "flex", flex: 1, gap: 0 },
    gameArea: { flex: 1, padding: "24px 32px", display: "flex", flexDirection: "column", gap: 20 },
    winBanner: {
        background: "rgba(45,198,83,0.15)",
        border: "1px solid #2dc653",
        color: "#2dc653",
        padding: "16px 24px",
        borderRadius: 8,
        textAlign: "center",
        fontSize: "1.2rem",
        fontWeight: 700,
        fontFamily: "'Exo 2', sans-serif",
    },
    opponents: { display: "flex", gap: 12, justifyContent: "center", flexWrap: "wrap" },
    opponentCard: {
        background: "#12121a",
        border: "1px solid #2a2a3d",
        borderRadius: 8,
        padding: "12px 20px",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: 4,
        minWidth: 120,
    },
    opponentName: { fontWeight: 700, fontFamily: "'Exo 2', sans-serif", fontSize: "0.95rem" },
    opponentCards: { color: "#8888aa", fontSize: "0.85rem" },
    table: {
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        gap: 40,
        padding: "30px",
        background: "#12121a",
        borderRadius: 12,
        border: "1px solid #2a2a3d",
        minHeight: 180,
    },
    pileContainer: { display: "flex", flexDirection: "column", alignItems: "center", gap: 8 },
    pileLabel: { color: "#8888aa", fontSize: "0.75rem", letterSpacing: "0.1em", textTransform: "uppercase" },
    emptyPile: {
        width: 80, height: 110,
        background: "#1a1a26",
        border: "2px dashed #2a2a3d",
        borderRadius: 8,
        display: "flex", alignItems: "center", justifyContent: "center",
        color: "#2a2a3d", fontSize: "1.5rem",
    },
    colorIndicator: { display: "flex", flexDirection: "column", alignItems: "center", gap: 8 },
    colorDot: { width: 48, height: 48, borderRadius: "50%", transition: "all 0.3s" },
    drawPile: {
        width: 80, height: 110,
        background: "#1a1a26",
        border: "2px solid #2a2a3d",
        borderRadius: 8,
        display: "flex", alignItems: "center", justifyContent: "center",
        fontSize: "2rem",
        transition: "all 0.2s",
    },
    handLabel: {
        color: "#8888aa",
        fontSize: "0.75rem",
        letterSpacing: "0.1em",
        textTransform: "uppercase",
        marginBottom: 12,
    },
    hand: { display: "flex", flexWrap: "wrap", gap: 8 },
    error: { color: "#e63946", fontSize: "0.85rem", textAlign: "center" },
};