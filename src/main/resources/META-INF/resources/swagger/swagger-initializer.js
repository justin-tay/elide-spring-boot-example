/*
 * Source code adapted from:
 * https://github.com/swagger-api/swagger-ui/blob/v5.1.3/dist/swagger-initializer.js
 */

window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  let search;
  if(parent.window) {
    search = parent.window.location.search;
  }
  if(!search) {
    search = window.location.search;
  }
  let path = new URLSearchParams(search).get('path');
  if(!path) {
    path = '';
  }

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    url: `../doc${path}${search}`,
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });

  //</editor-fold>
};
