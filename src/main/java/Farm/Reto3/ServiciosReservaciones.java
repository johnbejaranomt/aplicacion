/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Farm.Reto3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author JuanPablo
 * @version 1.1
 */
@Service
public class ServiciosReservaciones {
    /**
     * Creación de variable de tipo repositorio con la anotación
     */
    @Autowired
    private RepositorioReservaciones metodosCrud;
    
    /**
     * Método para obtener todos los datos de la tabla reservaciones
     * @return List de clase Reservaciones 
     */
    public List<Reservaciones> getAll() {
        return metodosCrud.getAll();
    }
    
    /**
     * Método para obtener dato de la tabla reservaciones por Id
     * @param reservationId
     * @return Optional de clase Reservaciones
     */
    public Optional<Reservaciones> getReservation(int reservationId) {
        return metodosCrud.getReservation(reservationId);
    }

    /**
     * Método para registrar valores en la tabla reservaciones
     * @param reservation
     * @return valor de clase Reservaciones
     */
    public Reservaciones save(Reservaciones reservation) {
        if (reservation.getIdReservation() == null) {
            return metodosCrud.save(reservation);
        } else {
            Optional<Reservaciones> e = metodosCrud.getReservation(reservation.getIdReservation());
            if (e.isEmpty()) {
                return metodosCrud.save(reservation);
            } else {
                return reservation;
            }
        }
    }

    /**
     * Método para actualizar un dato de la tabla Reservaciones
     * @param reservation
     * @return valor de clase Reservaciones
     */
    public Reservaciones update(Reservaciones reservation) {
        if (reservation.getIdReservation() != null) {
            Optional<Reservaciones> e = metodosCrud.getReservation(reservation.getIdReservation());
            if (!e.isEmpty()) {

                if (reservation.getStartDate() != null) {
                    e.get().setStartDate(reservation.getStartDate());
                }
                if (reservation.getDevolutionDate() != null) {
                    e.get().setDevolutionDate(reservation.getDevolutionDate());
                }
                if (reservation.getStatus() != null) {
                    e.get().setStatus(reservation.getStatus());
                }
                metodosCrud.save(e.get());
                return e.get();
            } else {
                return reservation;
            }
        } else {
            return reservation;
        }
    }
/**
 * Método para borrar un dato de la tabla Reservaciones por Id
 * @param reservationId
 * @return boolean
 */
    public boolean deleteReservation(int reservationId) {
        Boolean aBoolean = getReservation(reservationId).map(reservation -> {
            metodosCrud.delete(reservation);
            return true;
        }).orElse(false);
        return aBoolean;
    }

    /**
     * Método para adquirir el status del servicio
     * @return StatusReservas
     */
    public StatusReservas reporteStatusServicio() {
        List<Reservaciones> completed = metodosCrud.ReservacionStatusRepositorio("completed");
        List<Reservaciones> cancelled = metodosCrud.ReservacionStatusRepositorio("cancelled");

        return new StatusReservas(completed.size(), cancelled.size());
    }

    /**
     * Método para generar el reporte del tiempo de servicio
     * @param datoA
     * @param datoB
     * @return Lista reservaciones hechas
     */
    public List<Reservaciones> reporteTiempoServicio(String datoA, String datoB) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

        Date datoUno = new Date();
        Date datoDos = new Date();

        try {
            datoUno = parser.parse(datoA);
            datoDos = parser.parse(datoB);
        } catch (ParseException evt) {
            evt.printStackTrace();
        }
        if (datoUno.before(datoDos)) {
            return metodosCrud.ReservacionTiempoRepositorio(datoUno, datoDos);
        } else {
            return new ArrayList<>();

        }
    }

    /**
     * Método para 
     * @return Clientes que han recibido servicio
     */
    public List<ContadorClientes> reporteClientesServicio() {
        return metodosCrud.getClientesRepositorio();
    }
}
