(ns parinfer-site.parinfer)

(defn major-version []
  (aget js/window "parinfer" "version" "0"))

(defn- convert-changed-line [e]
  {:line-no (aget e "lineNo")
   :line (aget e "line")})

(defn- convert-error [e]
  (when e
    {:name (aget e "name")
     :message (aget e "message")
     :line-no (aget e "lineNo")
     :x (aget e "x")
     :extra (when-let [extra (aget e "extra")]
              {:name (aget extra "name")
               :line-no (aget extra "lineNo")
               :x (aget extra "x")})}))

(defn- convert-result [result]
  {:text (aget result "text")
   :cursor-x (aget result "cursorX")
   :success? (aget result "success")
   :changed-lines (mapv convert-changed-line (aget result "changedLines"))
   :error (convert-error (aget result "error"))})

(defn- convert-options [options]
  #js {:cursorX (:cursor-x options)
       :cursorLine (:cursor-line options)
       :prevCursorX (:prev-cursor-x options)
       :prevCursorLine (:prev-cursor-line options)
       :changes (clj->js (:changes options))
       :forceBalance (:force-balance options)})

(def smart-mode* (aget js/window "parinfer" "smartMode"))
(def indent-mode* (aget js/window "parinfer" "indentMode"))
(def paren-mode* (aget js/window "parinfer" "parenMode"))

(defn smart-mode
  ([text] (convert-result (smart-mode* text)))
  ([text options] (convert-result (smart-mode* text (convert-options options)))))

(defn indent-mode
  ([text] (convert-result (indent-mode* text)))
  ([text options] (convert-result (indent-mode* text (convert-options options)))))

(defn paren-mode
  ([text] (convert-result (paren-mode* text)))
  ([text options] (convert-result (paren-mode* text (convert-options options)))))
