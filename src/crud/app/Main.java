package crud.app;

import crud.enums.MenuOption;
import crud.model.Persona;
import crud.service.PersonaService;
import crud.exceptions.RepositoryException;
import crud.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static PersonaService service;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            service = new PersonaService();
        } catch (RepositoryException e) {
            System.err.println("No se pudo iniciar la aplicación: " + e.getMessage());
            return;
        }

        while (true) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione opción: ");
            MenuOption mo = MenuOption.fromCode(opcion);
            if (mo == null) {
                System.out.println("Opción inválida.\n");
                continue;
            }
            try {
                switch (mo) {
                    case SALIR -> {
                        System.out.println("Saliendo...");
                        return;
                    }
                    case CREAR -> accionCrear();
                    case LISTAR -> accionListar();
                    case BUSCAR -> accionBuscar();
                    case ACTUALIZAR -> accionActualizar();
                    case ELIMINAR -> accionEliminar();
                }
            } catch (RepositoryException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== CRUD PERSONAS =====");
        for (MenuOption m : MenuOption.values()) {
            System.out.printf("%d. %s%n", m.getCode(), m.getDesc());
        }
    }

    private static void accionCrear() throws RepositoryException {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        int edad = leerEntero("Edad: ");
        Persona p = service.crear(nombre, edad);
        System.out.println("Creado: " + p);
    }

    private static void accionListar() throws RepositoryException {
        List<Persona> list = service.listar();
        if (list.isEmpty()) {
            System.out.println("No hay personas registradas.");
            return;
        }
        list.forEach(System.out::println);
    }

    private static void accionBuscar() throws RepositoryException {
        int id = leerEntero("ID a buscar: ");
        Optional<Persona> opt = service.buscar(id);
        opt.ifPresentOrElse(
                p -> System.out.println("Encontrado: " + p),
                () -> System.out.println("No existe persona con id " + id)
        );
    }

    private static void accionActualizar() throws RepositoryException {
        int id = leerEntero("ID a actualizar: ");
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        int edad = leerEntero("Nueva edad: ");
        try {
            Persona p = service.actualizar(id, nombre, edad);
            System.out.println("Actualizado: " + p);
        } catch (EntityNotFoundException enf) {
            System.out.println("No se pudo actualizar: " + enf.getMessage());
        }
    }

    private static void accionEliminar() throws RepositoryException {
        int id = leerEntero("ID a eliminar: ");
        try {
            service.eliminar(id);
            System.out.println("Eliminado correctamente.");
        } catch (EntityNotFoundException enf) {
            System.out.println("No se pudo eliminar: " + enf.getMessage());
        }
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // limpiar buffer
        return val;
    }
}
