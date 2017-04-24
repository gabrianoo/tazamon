package com.tazamon.dav.cal.apple;

import com.tazamon.dav.cal.Calendar;
import com.tazamon.dav.web.DavAdapter;
import com.tazamon.dav.web.DavResponse;

import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class ListCalendarDavAdapter implements DavAdapter<List<Calendar>> {

    @Override
    public Optional<List<Calendar>> adapt(DavResponse davResponse) {
        return null;
    }
}
