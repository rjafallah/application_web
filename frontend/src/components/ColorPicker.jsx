const COLORS = [
    { value: "ROUGE", label: "Rouge", bg: "#e63946" },
    { value: "BLEU",  label: "Bleu",  bg: "#4361ee" },
    { value: "VERT",  label: "Vert",  bg: "#2dc653" },
    { value: "JAUNE", label: "Jaune", bg: "#f4a261" },
];

export default function ColorPicker({ onChoose, onClose }) {
    return (
        <div style={styles.overlay} onClick={onClose}>
            <div style={styles.modal} onClick={e => e.stopPropagation()}>
                <h3 style={styles.title}>Choisissez une couleur</h3>
                <div style={styles.grid}>
                    {COLORS.map(c => (
                        <button
                            key={c.value}
                            style={{ ...styles.colorBtn, background: c.bg }}
                            onClick={() => onChoose(c.value)}
                        >
                            {c.label}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
}

const styles = {
    overlay: {
        position: "fixed", inset: 0,
        background: "rgba(0,0,0,0.7)",
        display: "flex", alignItems: "center", justifyContent: "center",
        zIndex: 100,
    },
    modal: {
        background: "#12121a",
        border: "1px solid #2a2a3d",
        borderRadius: 12,
        padding: "32px 40px",
        textAlign: "center",
    },
    title: {
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 700,
        marginBottom: 24,
        fontSize: "1.1rem",
    },
    grid: { display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 },
    colorBtn: {
        padding: "16px 28px",
        border: "none",
        borderRadius: 8,
        color: "white",
        fontFamily: "'Exo 2', sans-serif",
        fontWeight: 700,
        fontSize: "0.95rem",
        cursor: "pointer",
        transition: "transform 0.15s",
    },
};