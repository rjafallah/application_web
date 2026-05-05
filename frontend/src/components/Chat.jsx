import { useState, useRef, useEffect } from "react";

export default function Chat({ messages, onSend }) {
    const [input, setInput] = useState("");
    const bottomRef = useRef(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    const handleSend = () => {
        if (!input.trim()) return;
        onSend(input.trim());
        setInput("");
    };

    return (
        <div style={styles.container}>
            <div style={styles.header}>💬 Chat</div>

            <div style={styles.messages}>
                {messages.length === 0 && (
                    <p style={styles.empty}>Aucun message</p>
                )}
                {messages.map((m, i) => (
                    <div key={i} style={styles.message}>
                        <span style={styles.sender}>{m.sender}</span>
                        <span style={styles.content}>{m.content}</span>
                    </div>
                ))}
                <div ref={bottomRef} />
            </div>

            <div style={styles.inputRow}>
                <input
                    className="input"
                    placeholder="Message..."
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    onKeyDown={e => e.key === "Enter" && handleSend()}
                    style={{ flex: 1, fontSize: "0.85rem", padding: "8px 12px" }}
                />
                <button
                    className="btn btn-primary"
                    onClick={handleSend}
                    style={{ padding: "8px 16px", fontSize: "0.8rem" }}
                >
                    Envoyer
                </button>
            </div>
        </div>
    );
}

const styles = {
    container: {
        width: 280,
        borderLeft: "1px solid #2a2a3d",
        background: "#12121a",
        display: "flex",
        flexDirection: "column",
    },
    header: {
        padding: "16px 20px",
        borderBottom: "1px solid #2a2a3d",
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 700,
        fontSize: "0.9rem",
        letterSpacing: "0.05em",
    },
    messages: {
        flex: 1,
        overflowY: "auto",
        padding: "12px",
        display: "flex",
        flexDirection: "column",
        gap: 8,
        maxHeight: "calc(100vh - 160px)",
    },
    empty: { color: "#8888aa", fontSize: "0.8rem", textAlign: "center", marginTop: 20 },
    message: { display: "flex", flexDirection: "column", gap: 2 },
    sender: {
        color: "#e63946",
        fontSize: "0.75rem",
        fontWeight: 700,
        fontFamily: "'Exo 2', sans-serif",
    },
    content: { color: "#e8e8f0", fontSize: "0.85rem" },
    inputRow: {
        display: "flex",
        gap: 8,
        padding: "12px",
        borderTop: "1px solid #2a2a3d",
    },
};