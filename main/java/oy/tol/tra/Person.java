package oy.tol.tra;


public class Person implements Comparable<Person> {
    // Source: https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function
    private static final int OFFSET_BASIS = 0x811C9DC5; // Popular FNV1 CONSTANT 2166136261. Doesn't overflow because it is stored as hexadecimal literal.
    private static final int PRIME = 0x01000193; // Popular FNV1 prime.
    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    @Override
    public int compareTo(Person o) {
        int thiss = this.hashCode();
        int other = o.hashCode();
        return thiss > other ? 1 : thiss == other ? 0 : -1;
    }

    public boolean equals(Object person) {
        if (person instanceof Person) {
            return this.firstName.equals(((Person) person).getFirstName())
                    &&
                    this.lastName.equals(((Person) person).getLastName());
        }
        return false;
    }

    public int hashCode() {
        String fullName = new StringBuilder().append(firstName).append(lastName).toString();
        return fullName.hashCode();
        //return FNV1aHash(fullName);
    }

    private int FNV1aHash(String name){
        int hash = OFFSET_BASIS;
        for (int i = 0; i < name.length(); i++) {
            hash ^= name.charAt(i);
            hash *= PRIME;
        }
        return hash;
    }
}
