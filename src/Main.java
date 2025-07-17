import java.util.Random;

// Абстрактный класс Room (запрещено создавать экземпляры)
abstract class Room {
    private final int roomNumber;
    private final int maxCapacity;
    private final int pricePerNight;
    private boolean isBooked;

    // Основной конструктор
    public Room(int roomNumber, int maxCapacity, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = maxCapacity;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    // Вспомогательный конструктор со случайной генерацией maxCapacity
    public Room(int roomNumber, int pricePerNight) {
        this(roomNumber, generateRandomCapacity(), pricePerNight);
    }

    // Генерация случайной вместимости (1-6 человек)
    private static int generateRandomCapacity() {
        return new Random().nextInt(6) + 1;
    }

    // Геттеры
    public int getRoomNumber() {
        return roomNumber;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public boolean isBooked() {
        return isBooked;
    }

    // Сеттер для бронирования
    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}

// Классы-наследники Room
class EconomyRoom extends Room {
    public EconomyRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

// Абстрактный класс (запрещено создавать экземпляры)
abstract class ProRoom extends Room {
    public ProRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

class StandardRoom extends Room {
    public StandardRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

class LuxRoom extends StandardRoom {
    public LuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

class UltraLuxRoom extends LuxRoom {
    public UltraLuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

// Пользовательское непроверяемое исключение
class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}

// Обобщенный интерфейс для работы с комнатами
interface RoomService<T extends Room> {
    void clean(T room);
    void reserve(T room);
    void free(T room);
}

// Реализация сервиса
class HotelService<T extends Room> implements RoomService<T> {
    @Override
    public void clean(T room) {
        System.out.println("Комната #" + room.getRoomNumber() + " убрана");
    }

    @Override
    public void reserve(T room) {
        if (room.isBooked()) {
            throw new RoomAlreadyBookedException(
                    "Комната #" + room.getRoomNumber() + " уже забронирована"
            );
        }
        room.setBooked(true);
        System.out.println("Комната #" + room.getRoomNumber() + " забронирована");
    }

    @Override
    public void free(T room) {
        room.setBooked(false);
        System.out.println("Комната #" + room.getRoomNumber() + " освобождена");
    }
}

// Тестовый класс
public class Main {
    public static void main(String[] args) {
        // Создаем сервис для работы с любыми комнатами
        HotelService<Room> hotelService = new HotelService<>();

        // Создаем различные типы комнат
        EconomyRoom economy = new EconomyRoom(101, 1500);
        StandardRoom standard = new StandardRoom(201, 3000);
        LuxRoom lux = new LuxRoom(301, 5000);
        UltraLuxRoom ultraLux = new UltraLuxRoom(401, 10000);

        // Тестируем сервис
        testRoomService(hotelService, economy);
        testRoomService(hotelService, standard);
        testRoomService(hotelService, lux);
        testRoomService(hotelService, ultraLux);

        // Попытка повторного бронирования
        try {
            hotelService.reserve(economy);
        } catch (RoomAlreadyBookedException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void testRoomService(HotelService<Room> service, Room room) {
        System.out.println("\nТестируем комнату #" + room.getRoomNumber() +
                " (" + room.getClass().getSimpleName() + ")");
        service.clean(room);
        service.reserve(room);
        service.free(room);
    }
}