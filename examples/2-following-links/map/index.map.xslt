<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">	
	
	<!-- 
	xmlns:java="http://xml.apache.org/xalan/java" 
	xmlns:notifications="org.vsearchd.crawler.extensions.Notifications"
	-->
	
	
	<!-- default parameter -->
	<xsl:param name="XmlSource" />
	<xsl:param name="SessionID" />
	<xsl:param name="HttpStatusCode" />
	
	<!-- custom xslt-parameter defined in project-request file  -->	
	<xsl:param name="client" /> 		
	
	<!--  defined in index.xml file -->
	<xsl:param name="HttpHost" />
	<xsl:param name="HttpDirPath" />
	<xsl:param name="ProjectPath" />

	<xsl:output omit-xml-declaration="no" method="xml" indent="yes"
		encoding="UTF-8" standalone="yes"   cdata-section-elements="field" />

	<xsl:template match="/">
		
		<mapping>
		
			<!--
			<xsl:variable name="debug">false</xsl:variable>
		
			<xsl:if test="$debug='true'">		    
				<xsl:value-of select="notifications:log('info', concat('Client: ', $client))" />
				<xsl:value-of select="notifications:log('info', concat('HttpStatusCode: ', $HttpStatusCode))" />
				<xsl:value-of select="notifications:log('info', concat('SessionID: ', $SessionID ))" />
				<xsl:value-of select="notifications:log('info', concat('ProjectPath: ', $ProjectPath ))" /> 	
				<xsl:value-of select="notifications:log('info', concat('XmlSource: ', normalize-space($XmlSource)))" />
			</xsl:if>
			-->
			
			<xsl:for-each select="//table//tr">
				
				<xsl:if test="./td[1]/text()">
			
					<xsl:variable name="vID"><xsl:value-of select="normalize-space(./td[1]/text())" /></xsl:variable>
					<xsl:variable name="vName"><xsl:value-of select="normalize-space(./td[2]/text())" /></xsl:variable>
					<xsl:variable name="vTitle"><xsl:value-of select="normalize-space(./td[3]/text())" /></xsl:variable>
					<xsl:variable name="vDescription"><xsl:value-of select="normalize-space(./td[4]/text())" /></xsl:variable>
					<xsl:variable name="vLink"><xsl:value-of select="normalize-space(./td[5]/a/@href)" /></xsl:variable>
					
					<!-- this creates a new http request for each record found -->
					<!--queue= 1 or 2 allowed; 1 has the highest priority !!! -->
					<server queue="1">
						<server-document>	
							
							<xslt-parameter name="ID" value="{$vID}" />
							<xslt-parameter name="Name" value="{$vName}" />
							<xslt-parameter name="Title" value="{$vTitle}" />
							<xslt-parameter name="Description" value="{$vDescription}" />
							<xslt-parameter name="client" value="{$client}" />
							
							<mapping file="{concat($ProjectPath,'details.map.xslt')}" />
							
							<urirequest name="initialRequest" root-tag="true"
								http-header="true" dtd="html" delay="6000" timeout="30000" retry="6"> 

								<source-engine name="tidy">
									<content-type value="text/html" />			
									<content-type value="text/html;charset=utf-8" />
								</source-engine>

								<method value="GET" />
								<field 	name="host" value="{$HttpHost}" />
								<field 	name="path" value="{concat($HttpDirPath,$vLink)}" />
								<field 	name="port" value="443" />
								<field 	name="protocol" value="HTTPS" />									
								<header name="User-Agent" value="vsearchd::crawler" />	
								<header name="Accept" value="text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5" />
								<header name="Accept-Charset" value="ISO-8859-1,utf-8;" />						
								<header name="Keep-Alive" value="false" />
								<header name="Connection" value="close" />
								
								<http-parameter name="client" value="{$client}" />
								
							</urirequest>
						</server-document>
					</server>
					
				</xsl:if>
			</xsl:for-each>
			     
		</mapping>	
	</xsl:template>
	
</xsl:stylesheet>
