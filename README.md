# Library Management System (LMS)
Completed as part of <b>IS3106: Enterprise Systems Interface Design and Development</b> coursework

Notable Features in this LMS Web Application include:

<li>1. Collections Function</li>
Allows Staff members to view the complete collection of books in the library if a member inquires about the existence of a book. This includes both currently available and borrowed books in the library.

2. Search Books by ISBN
Following traditional library conventions where the ISBN code of the book is scanned, to Loan a book to a member, the staff would have to enter the ISBN of the book. Clicking 'Search' would locate the book, where the borrow button will only be enabled if a valid book is found. Currently borrowed books will not be able to be found in this search bar.

3. Multiple Simultaneous Book Return
This application allows staff members to pick books members have on hand for return using checkboxes on the data table. This allows Book Return to be conducted more efficiently without all currently books needing to be returned simultaneously.

4. Function Filter
This ensures that staff members will be unable to access member-specific functions Lend Book and Return Book when there is no specific member being accessed. This requires Staff members to enter 'Member ID' in the pop-up dialog.

5. Member Session Scope
The Function Filter requires member details to be repeatedly entered. To increase accessibility, Session Scope is applied to the MemberManagedBean. This ensures that the 'Member ID' is pre-entered in the pop-up after the first entry, reducing the hassle of reentry if Loan and Return functions are being performed for the same user.

6. Pay Fine
This function assumes the Staff member is using another payment system to facilitate payments, and that fine payments for the books currently returned are completed in full. After payments, the books will be returned.

Assumptions

- Since the Staff member is conducting Return operations, the member is assumed to immediately pay the fine when prompted.

- After the fine is paid for, it is assumed that the member will definitely proceed with returning the book. This prevents any additional fine cumulation after this payment.

- This application assumes that there is no need to view fine amounts until the book is being returned. Correspondingly, fines cannot be paid for until a book is returned. This is to prevent additional fine cumulation after this payment, and multiple payments.
