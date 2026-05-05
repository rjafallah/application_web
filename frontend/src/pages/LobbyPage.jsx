import { useState, useEffect } from "react";
import { getWaitingGames, createGame, joinGame } from "../services/api";

export default function LobbyPage({ player, onJoinGame, onRanking }) {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        let active = true;

        const load = async () => {
            const data = await getWaitingGames();
            if (active) setGames(Array.isArray(data) ? data : []);
        };

        load();
        const interval = setInterval(load, 3000);
        return () => {
            active = false;
            clearInterval(interval);
        };
    }, []);

    const handleCreate = async () => {
        setLoading(true);
        const gameId = await createGame(player.id);
        onJoinGame(gameId);
        setLoading(false);
    };

    const handleJoin = async (gameId) => {
        await joinGame(gameId, player.id);
        onJoinGame(gameId);
    };

    return (
        <div style={styles.container}>
            {/* HEADER */}
            <div style={styles.header}>
                <div style={styles.logo}>
                    <span style={{ color: "#e8e8f0" }}>BLAZING</span>
                    <span style={{ color: "#e63946" }}>8s</span>
                </div>
                <div style={styles.playerInfo}>
                    <span style={styles.playerName}>👤 {player.username}</span>
                    <button className="btn btn-secondary" onClick={onRanking}>
                        Classement
                    </button>
                </div>
            </div>

            {/* CONTENT */}
            <div style={styles.content}>
                <div style={styles.titleRow}>
                    <h2 style={styles.title}>Parties disponibles</h2>
                    <button
                        className="btn btn-primary"
                        onClick={handleCreate}
                        disabled={loading}
                    >
                        {loading ? "..." : "+ Créer une partie"}
                    </button>
                </div>

                {/* LISTE DES PARTIES */}
                {games.length === 0 ? (
                    <div style={styles.empty}>
                        <p>Aucune partie en attente</p>
                        <p style={{ color: "#8888aa", fontSize: "0.85rem", marginTop: 8 }}>
                            Créez une nouvelle partie !
                        </p>
                    </div>
                ) : (
                    <div style={styles.gameList}>
                        {games.map(game => (
                            <div key={game.id} style={styles.gameCard}>
                                <div style={styles.gameInfo}>
                                    <span style={styles.gameId}>Partie #{game.id}</span>
                                    <span style={styles.gamePlayers}>
                                        👥 {game.playerCount} / 4 joueurs
                                    </span>
                                </div>
                                <div style={styles.gameStatus}>
                                    <span style={styles.statusBadge}>En attente</span>
                                    <button
                                        className="btn btn-success"
                                        onClick={() => handleJoin(game.id)}
                                        disabled={game.playerCount >= 4}
                                    >
                                        Rejoindre
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

const styles = {
    container: { minHeight: "100vh", background: "#0a0a0f" },
    header: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "20px 40px",
        borderBottom: "1px solid #2a2a3d",
        background: "#12121a",
    },
    logo: {
        fontSize: "1.8rem",
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 900,
    },
    playerInfo: { display: "flex", alignItems: "center", gap: 16 },
    playerName: {
        color: "#8888aa",
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 600,
    },
    content: { maxWidth: 800, margin: "0 auto", padding: "40px 20px" },
    titleRow: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        marginBottom: 24,
    },
    title: {
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 700,
        fontSize: "1.4rem",
        color: "#e8e8f0",
    },
    empty: {
        textAlign: "center",
        padding: "60px 20px",
        color: "#8888aa",
        border: "1px dashed #2a2a3d",
        borderRadius: 8,
    },
    gameList: { display: "flex", flexDirection: "column", gap: 12 },
    gameCard: {
        background: "#12121a",
        border: "1px solid #2a2a3d",
        borderRadius: 8,
        padding: "20px 24px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        transition: "border-color 0.2s",
    },
    gameInfo: { display: "flex", flexDirection: "column", gap: 6 },
    gameId: {
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 700,
        fontSize: "1.1rem",
    },
    gamePlayers: { color: "#8888aa", fontSize: "0.9rem" },
    gameStatus: { display: "flex", alignItems: "center", gap: 16 },
    statusBadge: {
        background: "rgba(244, 162, 97, 0.15)",
        color: "#f4a261",
        padding: "4px 12px",
        borderRadius: 20,
        fontSize: "0.8rem",
        fontWeight: 600,
        letterSpacing: "0.05em",
    },
};