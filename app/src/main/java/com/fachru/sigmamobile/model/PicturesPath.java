package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 10/12/15.
 */
@Table(name = "PicturesPath")
public class PicturesPath extends Model {

	@Column(name = "picture_name")
	public String picture_name;

	@Column(name = "picture_path")
	public String picture_path;

	@Column(name = "date")
	public Date picture_date;

	@Column(name = "latitude")
	public double picture_long;

	@Column(name = "longitude")
	public double picture_lat;

	@Column(name = "address")
	public String picture_address;

	public PicturesPath() {
		super();
	}

	public PicturesPath(String picture_name, String picture_path, String picture_address, double picture_long, double picture_lat) {
		super();
		this.picture_name = picture_name;
		this.picture_path = picture_path;
		this.picture_address = picture_address;
		this.picture_long = picture_long;
		this.picture_lat = picture_lat;
	}

	public static List<PicturesPath> all() {
		return new Select().from(PicturesPath.class).orderBy("date DESC").execute();
	}

	public void setDateFromString(String date) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		sf.setLenient(true);
		this.picture_date = sf.parse(date);
	}

	@Override
	public String toString() {
		return "PicturesPath{" +
				"picture_name='" + picture_name + '\'' +
				", picture_path='" + picture_path + '\'' +
				", picture_date=" + picture_date +
				", picture_long=" + picture_long +
				", picture_lat=" + picture_lat +
				", picture_address='" + picture_address + '\'' +
				'}';
	}
}
