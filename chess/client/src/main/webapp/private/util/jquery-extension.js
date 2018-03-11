"use strict";
$.escapeHtml = function(string) {
	return String(string).replace(/[&<>"'`=\/]/g, function(s) {
		return {
			'&' : '&amp;',
			'<' : '&lt;',
			'>' : '&gt;',
			'"' : '&quot;',
			"'" : '&#39;',
			'/' : '&#x2F;',
			'`' : '&#x60;',
			'=' : '&#x3D;'
		}[s];
	});
}