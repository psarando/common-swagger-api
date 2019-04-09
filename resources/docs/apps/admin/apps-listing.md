This service allows admins to list all public apps, including apps listed under the `Trash` category:
deleted public apps and private apps that are 'orphaned' (not categorized in any user's workspace).

If the `search` parameter is included, then the results are filtered by
the App name, description, integrator's name, tool name, or category name the app is under.

#### Delegates to metadata service
    POST /avus/filter-targets
    POST /ontologies/{ontology-version}/filter-targets
