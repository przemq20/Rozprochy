module Office {
    enum TaskType { RECEIVENEWID, CARREGISTRATION, SENDTAX };
    enum Status { RECEIVED, PROCESSED, DONE, ERROR };

    class Task {
        TaskType taskType;
        long id;
        optional(1) int time;
        Status status;
    };

    struct Account {
         long id;
         string name;
    };

    sequence<Task> Tasks;

    exception UserAlreadyExists {};
    exception UserNotExists {};
    exception TaskProccessingException {};

    interface ClientAccountManager {
        Task createTask(Account account, TaskType taskType);
        Tasks getStatus(Account account);
    };

    interface CallbackReceiver{
        void callback(Task task);
    };

    interface CallbackSender{
        void disconnect(Account account);
    };

    interface AccountsManager{
        Account register(string name, CallbackReceiver* callbackReceiver) throws UserAlreadyExists;
        Account login(string name, CallbackReceiver* callbackReceiver) throws UserNotExists;
    };

    struct DeliveryReceipt {
        Account account;
        long taskId;
    };

    interface TaskProcessor {
        void processTask(Task task, Account account) throws TaskProccessingException;
        void sendDeliveryReceipt(DeliveryReceipt deliveryReceipt);
    };
};