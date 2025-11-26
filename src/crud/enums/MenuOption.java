package crud.enums;

public enum MenuOption {
    SALIR(0, "Salir"),
    CREAR(1, "Crear persona"),
    LISTAR(2, "Listar personas"),
    BUSCAR(3, "Buscar por ID"),
    ACTUALIZAR(4, "Actualizar persona"),
    ELIMINAR(5, "Eliminar persona");

    private final int code;
    private final String desc;

    MenuOption(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() { return code; }
    public String getDesc() { return desc; }

    public static MenuOption fromCode(int code) {
        for (MenuOption m : values()) if (m.code == code) return m;
        return null;
    }
}
