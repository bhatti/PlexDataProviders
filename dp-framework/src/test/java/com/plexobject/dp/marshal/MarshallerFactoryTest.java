package com.plexobject.dp.marshal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.plexobject.dp.domain.DataFieldRow;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;

public class MarshallerFactoryTest {
    static class User {
        private String username;
        private String name;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                    + ((username == null) ? 0 : username.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            User other = (User) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (username == null) {
                if (other.username != null)
                    return false;
            } else if (!username.equals(other.username))
                return false;
            return true;
        }

    }

    static class UserMarshaller implements DataFieldRowMarshaller<User> {
        private MetaField name = MetaFieldFactory.create("name",
                MetaFieldType.SCALAR_TEXT);
        private MetaField username = MetaFieldFactory.create("username",
                MetaFieldType.SCALAR_TEXT);

        @Override
        public DataFieldRow marshal(User user) {
            DataFieldRow row = new DataFieldRow();
            row.addField(name, user.getName());
            row.addField(username, user.getUsername());
            return row;
        }

        @Override
        public User unmarshal(DataFieldRow from) {
            User user = new User();
            user.setName(from.getValueAsText(name));
            user.setUsername(from.getValueAsText(username));
            return user;
        }

    }

    @Test
    public void testMarshalUnmarshal() {
        User user = new User();
        user.setName("jake");
        user.setUsername("jake1000");
        MarshallerFactory.getInstance().register(User.class,
                DataFieldRow.class, new UserMarshaller());
        DataFieldRow row = MarshallerFactory.getInstance()
                .getMarshaller(User.class, DataFieldRow.class).marshal(user);
        User unmarshalledUser = MarshallerFactory.getInstance()
                .getMarshaller(User.class, DataFieldRow.class).unmarshal(row);
        assertEquals(user, unmarshalledUser);
    }

}
