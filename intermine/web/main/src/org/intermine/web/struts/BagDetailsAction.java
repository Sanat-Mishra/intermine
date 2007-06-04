package org.intermine.web.struts;

/*
 * Copyright (C) 2002-2007 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Map;

import org.intermine.objectstore.query.BagConstraint;
import org.intermine.objectstore.query.ConstraintOp;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.QueryClass;
import org.intermine.objectstore.query.SingletonResults;

import org.intermine.metadata.Model;
import org.intermine.model.InterMineObject;
import org.intermine.objectstore.ObjectStore;
import org.intermine.path.Path;
import org.intermine.web.logic.Constants;
import org.intermine.web.logic.bag.InterMineBag;
import org.intermine.web.logic.config.WebConfig;
import org.intermine.web.logic.profile.Profile;
import org.intermine.web.logic.results.PagedTable;
import org.intermine.web.logic.session.SessionMethods;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action that builds a PagedCollection to view a bag. Redirects to results.do
 *
 * @author Kim Rutherford
 * @author Thomas Riley
 */
public class BagDetailsAction extends Action
{
    /**
     * Set up session attributes for the bag details page.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return an ActionForward object defining where control goes next
     *
     * @exception Exception if an error occurs
     */
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 @SuppressWarnings("unused") ActionForm form,
                                 HttpServletRequest request,
                                 @SuppressWarnings("unused") HttpServletResponse response)
        throws Exception {
        HttpSession session = request.getSession();
        Profile profile = (Profile) session.getAttribute(Constants.PROFILE);
        ServletContext servletContext = session.getServletContext();
        ObjectStore os = (ObjectStore) servletContext.getAttribute(Constants.OBJECTSTORE);
        Model model = os.getModel();
        String bagName = request.getParameter("bagName");
        if (bagName == null) {
            bagName = request.getParameter("name");
        }
        InterMineBag bag = profile.getSavedBags().get(bagName);

        String identifier = "bag." + bagName;
        PagedTable pc = SessionMethods.getResultsTable(session, identifier);
        Map classKeys = (Map) servletContext.getAttribute(Constants.CLASS_KEYS);
        WebConfig webConfig = (WebConfig) servletContext.getAttribute(Constants.WEBCONFIG);
        Query q = new Query();
        QueryClass qc = new QueryClass(InterMineObject.class);
        q.addFrom(qc);
        q.addToSelect(qc);
        q.setConstraint(new BagConstraint(qc, ConstraintOp.IN, bag.getOsb()));
        q.setDistinct(false);
        SingletonResults res = os.executeSingleton(q);

        WebPathCollection webPathCollection =
            new WebPathCollection(os, new Path(model, bag.getType()), res, model, webConfig,
                                  classKeys);

        if (pc == null) {
            pc = new PagedTable(webPathCollection);
            SessionMethods.setResultsTable(session, identifier, pc);
        }

        return new ForwardParameters(mapping.findForward("results"))
                        .addParameter("table", identifier)
                        .addParameter("size", "25").forward();
    }
}
