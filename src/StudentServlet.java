import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/student")
public class StudentServlet extends HttpServlet {
    private final List<StudentDTO> studentsList = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        studentsList.add(new StudentDTO(1, "Alice", "alice@gmail.com", 25));
        studentsList.add(new StudentDTO(2, "Bob", "bob@gmail.com", 22));
        studentsList.add(new StudentDTO(3, "Charlie", "charlie@gmail.com", 19));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String ageString = req.getParameter("age");

        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"Name and email are required..!\"}");
        } else {
//        {
//          name:"example",
//          email:"example@gmail.com",
//          age:25,
//        }
            int id = studentsList.size() + 1;
            try {
                int age = Integer.parseInt(ageString);
                StudentDTO studentDTO = new StudentDTO(id, name, email, age);
                studentsList.add(studentDTO);
                resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
                resp.getWriter().write("{\"message\" : \"Student saved...!\"}");
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request ( Sending incorrect data from frontend  )
                resp.getWriter().write("{\"error\" : \"Invalid age\"}");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idString = req.getParameter("id");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String ageString = req.getParameter("age");

        System.out.println(idString);
        System.out.println(name);
        System.out.println(email);
        System.out.println(ageString);

        if (idString == null || idString.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request ( Sending incorrect data from frontend  )
            resp.getWriter().write("{\"error\" : \"Student id is required\"}");
        } else {
            try {
                int id = Integer.parseInt(idString);
                int age = Integer.parseInt(ageString);

                StudentDTO studentById = findStudentById(id);

                if (studentById == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    resp.getWriter().write("{\"error\" : \"Student not found\"}");
                } else {
                    studentById.setName(name);
                    studentById.setEmail(email);
                    studentById.setAge(age);

                    resp.setStatus(HttpServletResponse.SC_OK); // 200 ok
                    resp.getWriter().write("{\"message\" : \"Student updated...!\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request ( Sending incorrect data from frontend  )
                resp.getWriter().write("{\"error\" : \"Invalid student id or age\"}");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idString = req.getParameter("id");

        // id null or empty
        // invalid id
        // not found student

        if (idString == null || idString.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request ( Sending incorrect data from frontend  )
            resp.getWriter().write("{\"error\" : \"Student id is required\"}");
        } else {
            try {
                int id = Integer.parseInt(idString);
                StudentDTO studentById = findStudentById(id);
                if (studentById == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    resp.getWriter().write("{\"error\" : \"Student not found\"}");
                } else {
                    studentsList.remove(studentById);

                    resp.setStatus(HttpServletResponse.SC_OK); // 200 ok
                    resp.getWriter().write("{\"message\" : \"Student deleted...!\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request ( Sending incorrect data from frontend  )
                resp.getWriter().write("{\"error\" : \"Invalid student id\"}");
            }
        }
    }

    private StudentDTO findStudentById(int id) {
        for (StudentDTO studentDTO : studentsList) {
            if (studentDTO.getId() == id) {
                return studentDTO;
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");
//  [
//        {
//            'id' : 1,
//            'name' : 'Alice',
//             'email' : 'alice@gmail.com',
//             'age' : 25
//        },
//        {
//            'id' : 1,
//            'name' : 'Alice',
//             'email' : 'alice@gmail.com',
//             'age' : 25
//        }
//        ]

        StringBuilder studentListStringBuilder = new StringBuilder();
        studentListStringBuilder.append("[");
        for (int i = 0; i < studentsList.size(); i++) {
            StudentDTO studentDTO = studentsList.get(i);
//            String.format("Number %d", 1); // Number 1
//            String.format("Hello %s", "JavaEE"); // Hello JavaEE
            String studentDTOJsonString = String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\", \"age\": %d}",
                    studentDTO.getId(), //  'id' : 1,
                    studentDTO.getName(),  // 'name' : 'Alice',
                    studentDTO.getEmail(),
                    studentDTO.getAge()
            );
            studentListStringBuilder.append(studentDTOJsonString);
            if (i < studentsList.size() - 1) { // skip last object
                studentListStringBuilder.append(",");
            }
        }
        studentListStringBuilder.append("]");

        String jsonObectList = studentListStringBuilder.toString();
        printWriter.write(jsonObectList);

//        studentListStringBuilder.append("hello");
//        studentListStringBuilder.append("javeee");
    }

    //    public StudentServlet() {
//    }
}







