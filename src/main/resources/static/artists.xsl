<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    <xsl:template match="/ArrayList">
        <html>
            <head>
                <title>Artists List</title>
                <style>table { border-collapse: collapse; } th, td { border: 1px solid black; padding: 8px; }</style>
            </head>
            <body>
                <h1>All Artists</h1>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Birth Year</th>
                        <th>Pictures Count</th>
                    </tr>
                    <xsl:for-each select="item">
                        <tr>
                            <td><xsl:value-of select="id"/></td>
                            <td><a href="/api/artists/{id}"><xsl:value-of select="fullName"/></a></td>
                            <td><xsl:value-of select="birthYear"/></td>
                            <td><xsl:value-of select="count(pictures/item)"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
                <a href="/api/pictures">All Pictures</a>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>


