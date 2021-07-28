# airbnb-demo

# Project Description
You are going to build a web application for renting houses / places with the following functionalities.
1. This application will be used for renting houses for a user given check in - check out date period (kind of airbnb).
2. There will be 3 separate micro-services.
   * Authenticator - that will authenticate users.
   * Rental Processor - that will include all api / services for rental management.
   * Rental Search - that will include all api for searching houses, primarily handling
   booking requests and reviewing/rating houses. Booking requests will be forwarded to the rental processor provided api for actual booking request processing.
3. There will be two types of users. Admin & General registered users.
4. Rental Processor application will expose a list of api for following actions in rental
   management :
   * Submit a house as a host for rent with proper description and address by the
   registered user. Newly submitted rental request will be in Pending status. Proper validation should be added for this submitted request (i.e required fields, valid address, logical price etc). A house with Pending status should not be available in search results.
   1
   * Returns registered user’s submitted rental posts as a host (both list and single one in detail)
   * Approve & publish / Reject a rental post by the Admin. Approve & Publish action will make the rental post available for other user’s search results. Host will be notified through email. An api for returning pending rental posts should also be available for the admin.
   * Receiving a rental booking request from the registered user. Proper validation should be added for this submitted request (i.e house available for rental in the given Check In - Check out date range.) The house should not be already rented out for any part of the Check In - Check out date range.
   * Approve / Reject rental booking request by the admin. Proper email notification will be sent to the user who made the booking request. An api for returning pending booking requests should also be available for the admin.
   * Return registered user’s submitted booking requests (both list and single one in detail)
   * Cancel an already approved / pending booking request by the registered user who actually placed the booking. Cancelling should be done at least 7 days before the start of the Check In date. On cancelling the booking, house should be available on the user search result for cancelled booking’s Check In - Check out date range.
5. Rental Search application will expose a list of api for following actions in user search / review management:
   * Search a house with proper search criteria by the registered user. Search criteria should include address, date range, price range etc. Only the matched houses that are available in the date range will appear in the result. Users can also select a point in the map so all the rental houses around this point’s 1km radius can be searched. So the house's proper latitude / longitude should be saved and processed properly.
   * Submit a request to book a house for a valid future check In - check Out date period by the registered user. The selected house should be available for booking in all parts of the requested check In - check Out date range. (No approved or Pending booking should be available for any part of the submitted date range for the selected house). This application should redirect this request to the rental processor, and the response from the processor should be processed and returned (Inter service communication).
   * Submit a review and rating by the general user. An user can submit a review / rating for a house that he has already rented and visited (booking date range is already passed).
6. Functionality of Admin will be
   * Login using username / password.
   * Create another Admin account.
   * Can view a list of pending submitted rental posts.
   * Can view a list of pending booking requests.
   2 
     * Approve / Reject House rental submission request. 
       * Approve / Reject House booking request.
7. Registered Users have following functionality.
   * Registration in the system with proper email verification.
   * Log in with username / password.
   * Submit a house with proper description / address for rental as a host.
   * Search for a house to rent in a date period.
   * Can book an available house found in the search result.
   * Can cancel the user's own submitted pending / approved booking at least 7 days
   before the Check In date.
   * Can write a review on the already booked house by that user.
   * Can view the list of bookings that the user submitted.
   * Can view the list of rental posts that the user submitted as a host.
8. An admin user should be created on application initialization if no admin user exists in the system.
