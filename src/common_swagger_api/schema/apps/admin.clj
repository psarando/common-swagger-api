(ns common-swagger-api.schema.apps.admin
  (:use [common-swagger-api.schema
         :only [->optional-param
                optional-key->keyword
                describe
                SortFieldDocs
                SortFieldOptionalKey]]
        [common-swagger-api.schema.apps
         :only [AppBase
                AppDeletedParam
                AppDetails
                AppDisabledParam
                AppDocUrlParam
                AppListing
                AppListingDetail
                AppListingJobStats
                AppListingJobStatsDocs
                AppGroup
                AppReferencesParam
                AppSearchParams
                AppSearchValidSortFields
                GroupListDocs
                OptionalGroupsKey]]
        [schema.core :only [defschema enum optional-key]])
  (:require [clojure.set :as sets])
  (:import [java.util Date]))

(defschema AdminAppListingJobStats
  (merge AppListingJobStats
         {:job_count
          (describe Long "The number of times this app has run")

          :job_count_failed
          (describe Long "The number of times this app has run to `Failed` status")

          (optional-key :last_used)
          (describe Date "The start date this app was last run")}))

(defschema AdminAppListingDetail
  (merge AppListingDetail
         {(optional-key :job_stats)
          (describe AdminAppListingJobStats AppListingJobStatsDocs)}))

(defschema AdminAppListing
  (merge AppListing
         {:apps (describe [AdminAppListingDetail] "A listing of App details")}))

(def AdminAppListingJobStatsKeys (->> AdminAppListingJobStats
                                      keys
                                      (map optional-key->keyword)
                                      set))

(def AdminAppSearchValidSortFields
  (sets/union AppSearchValidSortFields AdminAppListingJobStatsKeys))

(def AppSubsets (enum :public :private :all))

(defschema AdminAppSearchParams
  (merge AppSearchParams
         {SortFieldOptionalKey
          (describe (apply enum AdminAppSearchValidSortFields) SortFieldDocs)

          (optional-key :app-subset)
          (describe AppSubsets "The subset of apps to search." :default :public)}))

(defschema AdminAppDetails
  (merge AppDetails
         {(optional-key :job_stats)
          (describe AdminAppListingJobStats AppListingJobStatsDocs)}))

(defschema AdminAppPatchRequest
  (-> AppBase
      (->optional-param :id)
      (->optional-param :name)
      (->optional-param :description)
      (assoc (optional-key :wiki_url)   AppDocUrlParam
             (optional-key :references) AppReferencesParam
             (optional-key :deleted)    AppDeletedParam
             (optional-key :disabled)   AppDisabledParam
             OptionalGroupsKey          (describe [AppGroup] GroupListDocs))))
