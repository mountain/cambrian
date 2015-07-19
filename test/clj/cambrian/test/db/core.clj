(ns cambrian.test.db.core
  (:require [clojure.test :refer :all]
            [cambrian.db.core :refer :all]
            [cambrian.db.migrations :as migrations]))

(deftest test-users
  (delete-user! {:id "1"})                           ;; Make sure the user with id 1 doesn't exist. You can also use transactions around tests to ensure that.
  (is (= 1 (create-user! {:id         "1"
                          :first_name "Sam"
                          :last_name  "Smith"
                          :email      "sam.smith@example.com"
                          :pass       "pass"})))
  (is (= (get-user {:id "1"})
         [{:id         "1"
           :first_name "Sam"
           :last_name  "Smith"
           :email      "sam.smith@example.com"
           :pass       "pass"
           :admin      nil
           :last_login nil
           :is_active  nil}])))

(use-fixtures :once (fn [f] (migrations/migrate ["migrate"]) (f)))
