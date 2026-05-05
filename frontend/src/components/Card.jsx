const colorMap = {
    ROUGE: { bg: "#e63946", shadow: "rgba(230,57,70,0.4)" },
    BLEU:  { bg: "#4361ee", shadow: "rgba(67,97,238,0.4)" },
    VERT:  { bg: "#2dc653", shadow: "rgba(45,198,83,0.4)" },
    JAUNE: { bg: "#f4a261", shadow: "rgba(244,162,97,0.4)" },
    NONE:  { bg: "#555577", shadow: "rgba(85,85,119,0.4)" },
};

const labelMap = {
    OSCARS_SWAP: "SWAP",
    GIDDY_UP: "SKIP",
    WRONG_WAY: "REV",
};

export default function Card({ card, onClick, disabled }) {
    const colors = colorMap[card.color] || colorMap.NONE;
    const label = labelMap[card.value] || card.value;
    const isSpecial = ["OSCARS_SWAP", "GIDDY_UP", "WRONG_WAY"].includes(card.value);
    const is8 = card.value === "8";

    return (
        <div
            onClick={!disabled ? onClick : undefined}
            style={{
                width: 72,
                height: 100,
                borderRadius: 8,
                background: colors.bg,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                flexDirection: "column",
                cursor: disabled ? "default" : "pointer",
                opacity: disabled ? 0.7 : 1,
                transition: "all 0.15s",
                boxShadow: disabled ? "none" : `0 4px 12px ${colors.shadow}`,
                transform: "translateY(0)",
                border: is8 ? "2px solid rgba(255,255,255,0.5)" : "none",
                userSelect: "none",
                position: "relative",
                overflow: "hidden",
            }}
            onMouseEnter={e => {
                if (!disabled) e.currentTarget.style.transform = "translateY(-8px)";
            }}
            onMouseLeave={e => {
                e.currentTarget.style.transform = "translateY(0)";
            }}
        >
            {/* Valeur principale */}
            <span style={{
                fontSize: isSpecial ? "0.65rem" : "1.6rem",
                fontWeight: 900,
                fontFamily: "'Exo 2', sans-serif",
                color: "white",
                textShadow: "0 2px 4px rgba(0,0,0,0.3)",
                letterSpacing: isSpecial ? "0.05em" : 0,
            }}>
                {label}
            </span>

            {/* Indicateur coin haut gauche */}
            <span style={{
                position: "absolute",
                top: 4,
                left: 6,
                fontSize: "0.7rem",
                fontWeight: 700,
                color: "rgba(255,255,255,0.7)",
                fontFamily: "'Exo 2', sans-serif",
            }}>
                {label}
            </span>
        </div>
    );
}