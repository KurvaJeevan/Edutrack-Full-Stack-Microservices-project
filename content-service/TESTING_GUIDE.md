# Content Service API Testing Guide

## Prerequisites

### 1. Services Running (in order):
- ✅ Discovery Service (Port 8761)
- ✅ Gateway Service (Port 8089)
- ✅ Authentication Service (Port 8071)
- ✅ Course Service (Dynamic port)
- ✅ Enrollment Service (Port 8089)
- ✅ Content Service (Port 1010)

### 2. Database Setup:
- MySQL running on localhost:3306
- Database `edutrack_content` created
- Database `auth_db` created (for user authentication)

### 3. Test Data Required:
- At least one user (INSTRUCTOR role) in auth_db
- At least one program in course service
- At least one course in that program
- At least one module in that course
- At least one student enrolled in the program

---

## Testing Steps

### STEP 1: Get JWT Token

**Endpoint:** `POST http://localhost:8071/api/auth/login`

**Request Body:**
```json
{
  "email": "instructor@example.com",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "INSTRUCTOR"
  },
  "statusCode": 200,
  "errors": null
}
```

**Action:** Copy the token value and use it in all subsequent requests.

---

### STEP 2: Create Content for Module (INSTRUCTOR/ADMIN)

**Endpoint:** `POST http://localhost:1010/api/content/module/{moduleId}`

**Headers:**
```
Authorization: Bearer <your-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "contentType": "Video",
  "title": "Introduction to Spring Boot",
  "contentUri": "https://example.com/videos/spring-boot-intro.mp4",
  "duration": 45.5,
  "status": "Published"
}
```

**Valid Content Types:** Video, PDF, Slide, Lab
**Valid Status:** Draft, Published

**Expected Response (Success):**
```json
{
  "success": true,
  "message": "Content Saved for Module 1",
  "data": {
    "contentId": 1,
    "moduleId": 1,
    "contentType": "Video",
    "title": "Introduction to Spring Boot",
    "contentUri": "https://example.com/videos/spring-boot-intro.mp4",
    "duration": 45.5,
    "status": "Published"
  },
  "statusCode": 201,
  "errors": []
}
```

**Test Cases:**
1. ✅ Valid data with INSTRUCTOR token
2. ✅ Valid data with ADMIN token
3. ❌ Invalid contentType (e.g., "Audio") - Should fail validation
4. ❌ Invalid status (e.g., "Active") - Should fail validation
5. ❌ Missing required fields - Should fail validation
6. ❌ STUDENT token - Should return 403 Forbidden
7. ❌ No token - Should return 401 Unauthorized

---

### STEP 3: Get Content by ID (ALL ROLES)

**Endpoint:** `GET http://localhost:1010/api/content/{contentId}`

**Headers:**
```
Authorization: Bearer <your-token>
```

**Expected Response (INSTRUCTOR/ADMIN):**
```json
{
  "success": true,
  "message": "Content fetched successfully",
  "data": {
    "contentId": 1,
    "moduleId": 1,
    "contentType": "Video",
    "title": "Introduction to Spring Boot",
    "contentUri": "https://example.com/videos/spring-boot-intro.mp4",
    "duration": 45.5,
    "status": "Published"
  },
  "statusCode": 200,
  "errors": []
}
```

**Expected Response (STUDENT - Not Enrolled):**
```json
{
  "success": false,
  "message": "Access Denied: You are not enrolled in this program.",
  "data": null,
  "statusCode": 403,
  "errors": ["Enrollment required"]
}
```

**Test Cases:**
1. ✅ INSTRUCTOR token - Should return content
2. ✅ ADMIN token - Should return content
3. ✅ STUDENT token (enrolled) - Should return content
4. ❌ STUDENT token (not enrolled) - Should return 403
5. ❌ Invalid contentId - Should return 404
6. ❌ No token - Should return 401

---

### STEP 4: Get All Content by Module ID (ALL ROLES)

**Endpoint:** `GET http://localhost:1010/api/content/module/{moduleId}`

**Headers:**
```
Authorization: Bearer <your-token>
```

**Expected Response (Success):**
```json
{
  "success": true,
  "message": "Content list fetched successfully",
  "data": [
    {
      "contentId": 1,
      "moduleId": 1,
      "contentType": "Video",
      "title": "Introduction to Spring Boot",
      "contentUri": "https://example.com/videos/spring-boot-intro.mp4",
      "duration": 45.5,
      "status": "Published"
    },
    {
      "contentId": 2,
      "moduleId": 1,
      "contentType": "PDF",
      "title": "Spring Boot Documentation",
      "contentUri": "https://example.com/docs/spring-boot.pdf",
      "duration": 0.0,
      "status": "Published"
    }
  ],
  "statusCode": 200,
  "errors": []
}
```

**Test Cases:**
1. ✅ INSTRUCTOR token - Should return all content
2. ✅ ADMIN token - Should return all content
3. ✅ STUDENT token (enrolled) - Should return all content
4. ❌ STUDENT token (not enrolled) - Should return 403
5. ✅ Module with no content - Should return empty array
6. ❌ No token - Should return 401

---

### STEP 5: Update Content (INSTRUCTOR/ADMIN)

**Endpoint:** `PUT http://localhost:1010/api/content/{contentId}`

**Headers:**
```
Authorization: Bearer <your-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "moduleId": 1,
  "contentType": "Video",
  "title": "Advanced Spring Boot Concepts",
  "contentUri": "https://example.com/videos/spring-boot-advanced.mp4",
  "duration": 60.0,
  "status": "Published"
}
```

**Expected Response (Success):**
```json
{
  "success": true,
  "message": "Content Updated Successfully",
  "data": {
    "contentId": 1,
    "moduleId": 1,
    "contentType": "Video",
    "title": "Advanced Spring Boot Concepts",
    "contentUri": "https://example.com/videos/spring-boot-advanced.mp4",
    "duration": 60.0,
    "status": "Published"
  },
  "statusCode": 200,
  "errors": []
}
```

**Expected Response (ModuleId Change Attempt):**
```json
{
  "success": false,
  "message": "ModuleId cannot be changed once content is created",
  "data": null,
  "statusCode": 400,
  "errors": []
}
```

**Test Cases:**
1. ✅ Valid update with INSTRUCTOR token
2. ✅ Valid update with ADMIN token
3. ❌ Change moduleId - Should fail with 400
4. ❌ Invalid contentId - Should return 404
5. ❌ STUDENT token - Should return 403
6. ❌ No token - Should return 401

---

### STEP 6: Delete Content (INSTRUCTOR/ADMIN)

**Endpoint:** `DELETE http://localhost:1010/api/content/{contentId}`

**Headers:**
```
Authorization: Bearer <your-token>
```

**Expected Response (Success):**
```json
{
  "success": true,
  "message": "Content Deleted Successfully",
  "data": {
    "contentId": 1,
    "moduleId": 1,
    "contentType": "Video",
    "title": "Advanced Spring Boot Concepts",
    "contentUri": "https://example.com/videos/spring-boot-advanced.mp4",
    "duration": 60.0,
    "status": "Published"
  },
  "statusCode": 200,
  "errors": []
}
```

**Test Cases:**
1. ✅ Valid delete with INSTRUCTOR token
2. ✅ Valid delete with ADMIN token
3. ❌ Invalid contentId - Should return 404
4. ❌ Delete same content twice - Should return 404
5. ❌ STUDENT token - Should return 403
6. ❌ No token - Should return 401

---

## Complete Test Scenarios

### Scenario 1: Instructor Creates and Manages Content

1. Login as INSTRUCTOR → Get token
2. Create Video content for module 1
3. Create PDF content for module 1
4. Get all content for module 1 → Should see 2 items
5. Update Video content title
6. Get content by ID → Should see updated title
7. Delete PDF content
8. Get all content for module 1 → Should see 1 item

### Scenario 2: Student Access Control

1. Login as STUDENT (enrolled in program) → Get token
2. Get content by ID → Should succeed
3. Get all content for module → Should succeed
4. Try to create content → Should fail (403)
5. Try to update content → Should fail (403)
6. Try to delete content → Should fail (403)

### Scenario 3: Student Not Enrolled

1. Login as STUDENT (NOT enrolled) → Get token
2. Get content by ID → Should fail (403)
3. Get all content for module → Should fail (403)

### Scenario 4: Validation Testing

1. Create content with invalid contentType → Should fail
2. Create content with invalid status → Should fail
3. Create content with title < 3 chars → Should fail
4. Create content with title > 100 chars → Should fail
5. Create content with negative duration → Should fail
6. Update content changing moduleId → Should fail

---

## Postman Testing

### Import Collection:
1. Open Postman
2. Click Import
3. Select `Content-Service-API-Tests.postman_collection.json`
4. Collection will be imported with all endpoints

### Set Variables:
1. Click on collection → Variables tab
2. Set `baseUrl` = `http://localhost:1010`
3. Set `token` = `<your-jwt-token>` (after login)

### Run Tests:
1. Execute requests in order
2. Check response status codes
3. Verify response body structure
4. Test different user roles

---

## Manual Testing with cURL

### 1. Create Content:
```bash
curl -X POST http://localhost:1010/api/content/module/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "contentType": "Video",
    "title": "Introduction to Spring Boot",
    "contentUri": "https://example.com/videos/spring-boot-intro.mp4",
    "duration": 45.5,
    "status": "Published"
  }'
```

### 2. Get Content by ID:
```bash
curl -X GET http://localhost:1010/api/content/1 \
  -H "Authorization: Bearer <token>"
```

### 3. Get Content by Module:
```bash
curl -X GET http://localhost:1010/api/content/module/1 \
  -H "Authorization: Bearer <token>"
```

### 4. Update Content:
```bash
curl -X PUT http://localhost:1010/api/content/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "moduleId": 1,
    "contentType": "Video",
    "title": "Advanced Spring Boot",
    "contentUri": "https://example.com/videos/advanced.mp4",
    "duration": 60.0,
    "status": "Published"
  }'
```

### 5. Delete Content:
```bash
curl -X DELETE http://localhost:1010/api/content/1 \
  -H "Authorization: Bearer <token>"
```

---

## Expected HTTP Status Codes

| Scenario | Status Code | Description |
|----------|-------------|-------------|
| Successful GET | 200 | OK |
| Successful POST | 201 | Created |
| Successful PUT | 200 | OK |
| Successful DELETE | 200 | OK |
| Validation Error | 400 | Bad Request |
| No Token | 401 | Unauthorized |
| Wrong Role | 403 | Forbidden |
| Not Enrolled | 403 | Forbidden |
| Resource Not Found | 404 | Not Found |
| Server Error | 500 | Internal Server Error |

---

## Troubleshooting

### Issue: 401 Unauthorized
- Check if token is valid
- Check if token is expired
- Verify Authorization header format: `Bearer <token>`

### Issue: 403 Forbidden
- Check user role (STUDENT cannot create/update/delete)
- For STUDENT: Check if enrolled in program

### Issue: 404 Not Found
- Verify contentId exists
- Verify moduleId exists in course service

### Issue: 400 Bad Request
- Check request body format
- Verify all required fields present
- Check field validations (contentType, status, title length, etc.)

### Issue: 500 Internal Server Error
- Check if Course Service is running (for Feign calls)
- Check if Enrollment Service is running (for Feign calls)
- Check database connection
- Check application logs

---

## Testing Checklist

- [ ] All services are running
- [ ] MySQL database is accessible
- [ ] Test data is created (users, programs, courses, modules)
- [ ] JWT token obtained from login
- [ ] Token added to Postman/cURL requests
- [ ] Test with INSTRUCTOR role
- [ ] Test with ADMIN role
- [ ] Test with STUDENT role (enrolled)
- [ ] Test with STUDENT role (not enrolled)
- [ ] Test without token
- [ ] Test all validation scenarios
- [ ] Test error scenarios (404, 400, 403)
- [ ] Verify Feign client calls work
- [ ] Check database records after operations

---

## Success Criteria

✅ All endpoints return correct status codes
✅ Response body structure matches expected format
✅ Role-based access control works correctly
✅ Enrollment validation works for students
✅ Feign client calls to Course/Enrollment services succeed
✅ Database records are created/updated/deleted correctly
✅ Validation errors are handled properly
✅ JWT authentication works across all endpoints
