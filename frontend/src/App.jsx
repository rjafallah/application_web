import { useState } from "react";
import LoginPage from "./pages/LoginPage";
import LobbyPage from "./pages/LobbyPage";
import GamePage from "./pages/GamePage";
import RankingPage from "./pages/RankingPage";
import "./App.css";

export default function App() {
    const [player, setPlayer] = useState(() => {
        const saved = localStorage.getItem("player");
        return saved ? JSON.parse(saved) : null;
    });

    const [page, setPage] = useState(() => {
        return localStorage.getItem("player") ? "lobby" : "login";
    });
    const [gameId, setGameId] = useState(null);

    const goToLobby = (p) => {
        localStorage.setItem("player", JSON.stringify(p));
        setPlayer(p);
        setPage("lobby");
    };
    const goToGame = (gId) => { setGameId(gId); setPage("game"); };
    const goToRanking = () => setPage("ranking");
    const goToLobbyFromGame = () => setPage("lobby");

    const logout = () => {
        localStorage.removeItem("player");
        setPlayer(null);
        setPage("login");
    };

    return (
        <div className="app">
            {page === "login" && <LoginPage onLogin={goToLobby} />}
            {page === "lobby" && (
                <LobbyPage
                    player={player}
                    onJoinGame={goToGame}
                    onRanking={goToRanking}
                    onLogout={logout}
                />
            )}
            {page === "game" && (
                <GamePage
                    gameId={gameId}
                    player={player}
                    onLeave={goToLobbyFromGame}
                    onLogout={logout}
                />
            )}
            {page === "ranking" && (
                <RankingPage onBack={() => setPage("lobby")} 
                onLogout={logout}
                />
            )}
        </div>
    );
}