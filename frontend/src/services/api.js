const BASE = "http://localhost:8080/api";

// AUTH
export const register = (username, password) =>
    fetch(`${BASE}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    }).then(r => r.json());

export const login = (username, password) =>
    fetch(`${BASE}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    }).then(r => r.json());

// LOBBY
export const getWaitingGames = () =>
    fetch(`${BASE}/lobby`).then(r => r.json());

export const createGame = (playerId) =>
    fetch(`${BASE}/lobby/create?playerId=${playerId}`, {
        method: "POST"
    }).then(r => r.json());

export const joinGame = (gameId, playerId) =>
    fetch(`${BASE}/lobby/join/${gameId}?playerId=${playerId}`, {
        method: "POST"
    }).then(r => r.json());

// GAME
export const startGame = (gameId) =>
    fetch(`${BASE}/game/${gameId}/start`, {
        method: "POST"
    }).then(r => r.text());

export const getGameState = (gameId, playerId) =>
    fetch(`${BASE}/game/${gameId}?playerId=${playerId}`).then(r => r.json());

export const playCard = (gameId, playerId, cardId, chosenColor) => {
    let url = `${BASE}/game/${gameId}/play?playerId=${playerId}&cardId=${cardId}`;
    if (chosenColor) url += `&chosenColor=${chosenColor}`;
    return fetch(url, { method: "POST" })
        .then(r => {
            if (!r.ok) return r.text().then(msg => { throw new Error(msg); });
            return r.text();
        });
};

export const drawCard = (gameId, playerId) =>
    fetch(`${BASE}/game/${gameId}/draw?playerId=${playerId}`, {
        method: "POST"
    }).then(r => {
        if (!r.ok) return r.text().then(msg => { throw new Error(msg); });
        return r.json();
    });

// RANKING
export const getRanking = () =>
    fetch(`${BASE}/ranking`).then(r => r.json());