import { useState, useEffect } from "react";
import { getRanking } from "../services/api";

export default function RankingPage({ onBack }) {
    const [ranking, setRanking] = useState([]);

    useEffect(() => {
        getRanking().then(data => setRanking(Array.isArray(data) ? data : []));
    }, []);

    const medals = ["🥇", "🥈", "🥉"];

    return (
        <div style={styles.container}>
            <div style={styles.header}>
                <button className="btn btn-secondary" onClick={onBack}>← Retour</button>
                <div style={styles.logo}>
                    <span style={{ color: "#e8e8f0" }}>BLAZING</span>
                    <span style={{ color: "#e63946" }}>8s</span>
                </div>
                <div style={{ width: 100 }} />
            </div>

            <div style={styles.content}>
                <h2 style={styles.title}>🏆 Classement</h2>
                <div style={styles.table}>
                    <div style={styles.tableHeader}>
                        <span>#</span>
                        <span>Joueur</span>
                        <span>Victoires</span>
                        <span>Défaites</span>
                        <span>Parties</span>
                    </div>
                    {ranking.map((r, i) => (
                        <div key={i} style={{
                            ...styles.tableRow,
                            background: i === 0 ? "rgba(244,162,97,0.05)" : "transparent",
                        }}>
                            <span style={styles.rank}>
                                {i < 3 ? medals[i] : `#${i + 1}`}
                            </span>
                            <span style={styles.username}>{r.username}</span>
                            <span style={{ color: "#2dc653", fontWeight: 700 }}>{r.totalWins}</span>
                            <span style={{ color: "#e63946" }}>{r.totalLosses}</span>
                            <span style={{ color: "#8888aa" }}>{r.totalGames}</span>
                        </div>
                    ))}
                    {ranking.length === 0 && (
                        <div style={styles.empty}>Aucune donnée disponible</div>
                    )}
                </div>
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
    logo: { fontSize: "1.8rem", fontFamily: "'Exo 2', sans-serif", fontWeight: 900 },
    content: { maxWidth: 700, margin: "0 auto", padding: "40px 20px" },
    title: {
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 700,
        fontSize: "1.6rem",
        marginBottom: 24,
        textAlign: "center",
    },
    table: {
        background: "#12121a",
        border: "1px solid #2a2a3d",
        borderRadius: 8,
        overflow: "hidden",
    },
    tableHeader: {
        display: "grid",
        gridTemplateColumns: "60px 1fr 100px 100px 100px",
        padding: "14px 20px",
        borderBottom: "1px solid #2a2a3d",
        color: "#8888aa",
        fontSize: "0.8rem",
        letterSpacing: "0.1em",
        textTransform: "uppercase",
    },
    tableRow: {
        display: "grid",
        gridTemplateColumns: "60px 1fr 100px 100px 100px",
        padding: "16px 20px",
        borderBottom: "1px solid #1a1a26",
        alignItems: "center",
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 600,
    },
    rank: { fontSize: "1.1rem" },
    username: { fontWeight: 700 },
    empty: { padding: "40px", textAlign: "center", color: "#8888aa" },
};