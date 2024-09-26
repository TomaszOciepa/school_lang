export const environment = {
  production: false,
  // apiUrlStudents: 'http://localhost:8091/student',
  apiUrlStudents: 'http://localhost:9000/student-service/student',

  // apiUrlTeacher: 'http://localhost:8092/teacher',
  apiUrlTeacher: 'http://localhost:9000/teacher-service/teacher',

  // apiUrlCourses: 'http://localhost:8093/course',
  apiUrlCourses: 'http://localhost:9000/course-service/course',

  // apiUrlLessons: 'http://localhost:8094/calendar',
  apiUrlLessons: 'http://localhost:9000/calendar-service/calendar',

  // apiUrlOrder: 'http://localhost:8097/order',
  apiUrlOrder: 'http://localhost:9000/order-service/order',

  // keycloakClientService: 'http://localhost:8095/keycloak-client-service/keycloak',
  keycloakClientService:
    'http://localhost:9000/keycloak-client-service/keycloak',
};
