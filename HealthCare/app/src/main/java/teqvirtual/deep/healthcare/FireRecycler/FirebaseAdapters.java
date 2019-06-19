package teqvirtual.deep.healthcare.FireRecycler;




import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseArray;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface FirebaseAdapters<T> extends ChangeEventListener {
    /**
     * If you need to do some setup before the adapter starts listening for change events in the
     * database, do so it here and then call {@code super.startListening()}.
     */
    void startListening();

    /**
     * Removes listeners and clears all items in the backing {@link FirebaseArray}.
     */
    void cleanup();

    T getItem(int position);

    DatabaseReference getRef(int position);
}
