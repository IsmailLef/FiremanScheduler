package simulation;

public abstract class Event implements Comparable<Event> {

    protected long date; // The time when the event ends

    /* Constructor */
    public Event(long date) {
        this.date = date;
    }

    /* Getter */
    public long getDate() {
        return this.date;
    }

    /* Useful methods */

    /**
     * The method that execute the event depending on its type
     */
    public abstract void execute();
    
    /**
     * @param date1
     * @param date2
     * @return the maximum value of date1 et date2
     */
    public static long max(long date1, long date2) {
        if (date1 < date2) return date2;
        else return date1;
    }

    /**
     * The compareTo method for events 
     */
    @Override
    public int compareTo(Event e1) {
        if (e1.getDate() < date) {
            return 1;
        }
        if (e1.getDate() > date) {
            return -1;
        }
        return 0;
    }
    }

