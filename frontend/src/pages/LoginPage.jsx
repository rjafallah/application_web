import { useState } from "react";
import { login, register } from "../services/api";

export default function LoginPage({ onLogin }) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [mode, setMode] = useState("login"); // login ou register
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        if (!username.trim() || !password.trim()) {
            setError("Veuillez remplir le nom d'utilisateur et le mot de passe");
            return;
        }

        setLoading(true);
        setError("");

        try {
            const fn = mode === "login" ? login : register;
            const player = await fn(username.trim(), password);

            if (player?.id) {
                onLogin(player);
            } else {
                setError("Réponse invalide du serveur");
            }
        } catch (e) {
            setError(e.message || "Erreur de connexion au serveur");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                {/* LOGO */}
                <div style={styles.logo}>
                    <span style={styles.logoAccent}>BLAZING</span>
                    <span style={styles.logo8}>8s</span>
                </div>
                <p style={styles.subtitle}>Jeu de cartes multijoueur</p>

                {/* TABS */}
                <div style={styles.tabs}>
                    <button
                        style={{...styles.tab, ...(mode === "login" ? styles.tabActive : {})}}
                        onClick={() => setMode("login")}
                    >Connexion</button>
                    <button
                        style={{...styles.tab, ...(mode === "register" ? styles.tabActive : {})}}
                        onClick={() => setMode("register")}
                    >Inscription</button>
                </div>

                {/* FORM */}
                <div style={styles.form}>
                    <input
                        className="input"
                        placeholder="Nom d'utilisateur"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        onKeyDown={e => e.key === "Enter" && handleSubmit()}
                    />
                    <input
                        className="input"
                        placeholder="Mot de passe"
                        type="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        onKeyDown={e => e.key === "Enter" && handleSubmit()}
                        style={{ marginTop: 12 }}
                    />

                    {error && <p style={styles.error}>{error}</p>}

                    <button
                        className="btn btn-primary"
                        onClick={handleSubmit}
                        disabled={loading}
                        style={{ width: "100%", marginTop: 20 }}
                    >
                        {loading ? "..." : mode === "login" ? "Se connecter" : "S'inscrire"}
                    </button>
                </div>
            </div>
        </div>
    );
}

const styles = {
    container: {
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: "radial-gradient(ellipse at center, #1a1a2e 0%, #0a0a0f 70%)",
    },
    card: {
        background: "#12121a",
        border: "1px solid #2a2a3d",
        borderRadius: 12,
        padding: "48px 40px",
        width: 380,
        textAlign: "center",
        boxShadow: "0 20px 60px rgba(0,0,0,0.5)",
    },
    logo: {
        fontSize: "3rem",
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 900,
        letterSpacing: "0.05em",
        marginBottom: 8,
    },
    logoAccent: { color: "#e8e8f0" },
    logo8: {
        color: "#e63946",
        textShadow: "0 0 30px rgba(230,57,70,0.6)",
    },
    subtitle: {
        color: "#8888aa",
        fontSize: "0.9rem",
        marginBottom: 32,
        letterSpacing: "0.1em",
        textTransform: "uppercase",
    },
    tabs: {
        display: "flex",
        marginBottom: 24,
        borderRadius: 6,
        overflow: "hidden",
        border: "1px solid #2a2a3d",
    },
    tab: {
        flex: 1,
        padding: "10px",
        background: "transparent",
        border: "none",
        color: "#8888aa",
        cursor: "pointer",
        fontFamily: "'Rajdhani', sans-serif",
        fontSize: "0.95rem",
        fontWeight: 600,
        letterSpacing: "0.05em",
        transition: "all 0.2s",
    },
    tabActive: {
        background: "#e63946",
        color: "white",
    },
    form: { textAlign: "left" },
    error: {
        color: "#e63946",
        fontSize: "0.85rem",
        marginTop: 10,
    },
};