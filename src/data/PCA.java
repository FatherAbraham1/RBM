package data;

import java.util.ArrayList;
import java.util.List;

import tools.Tools;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;

public class PCA {
	private int numPrincipleComponents;
	private Data data;
	private Data transformedData;
	private List<Double> featureMeans;

	private DoubleMatrix2D covarianceMatrix;
	private EigenvalueDecomposition eigenValueDecomp;
	private DoubleMatrix2D principleMatrix;
	private DoubleMatrix2D transformationMatrix;

	public PCA(int numPrincipleComponents) {
		this.numPrincipleComponents = numPrincipleComponents;
	}

	public Data runPCA(Data data) {
		this.data = data;
		transformedData = new Data();
		centerAtZero();
		calculateCovarianceMatrix();
		calculateEigenVectors();
		findPrincipleComponents();
		constructTransformationMatrix();
		transformData();
		return transformedData;
	}
	
	private void transformData() {
		for (Datapoint datapoint : data.datapoints) {
			Datapoint newPoint = transformDataPoint(datapoint);
			transformedData.add(newPoint.getFeatures(), newPoint.getLabel());
		}
	}
	
	private Datapoint transformDataPoint(Datapoint datapoint) {
		
		int[] features = datapoint.getFeatures();
		double[][] difference = new double[1][features.length];
		for (int i = 0; i < features.length; i++) {
			difference[0][i] = features.length;
		}
		DoubleMatrix2D diff = new DenseDoubleMatrix2D(difference);
		DoubleMatrix2D newVector = null;
		
		newVector = diff.zMult(transformationMatrix, newVector);
		
		int[] newFeatures = new int[newVector.columns()];
		for (int i = 0; i < newVector.columns(); i++) {
			newFeatures[i] = Tools.toInt(newVector.get(0, i));
		}
		
		return new Datapoint(newFeatures, datapoint.getLabel());
	}
	
	private void constructTransformationMatrix() {
		double[][] transformationMatrix = new double[principleMatrix.rows()][numPrincipleComponents];
		int rows = transformationMatrix.length;
		int columns = transformationMatrix[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				transformationMatrix[i][j] = principleMatrix.get(i, j + (columns - numPrincipleComponents));
			}
		}
		this.transformationMatrix = new DenseDoubleMatrix2D(transformationMatrix);
	}

	private void centerAtZero() {
		featureMeans = new ArrayList<>();
		for (int i = 0; i < data.get(0).getFeatures().length; i++) {
			featureMeans.add(new Double(0.0));
		}
		for (Datapoint dataPoint : data.datapoints) {
			for (int i = 0; i < dataPoint.getFeatures().length; i++) {
				double currentSum = featureMeans.get(i);
				featureMeans.set(i, currentSum + dataPoint.getFeatures()[i]);
			}
		}
		for (int i = 0; i < featureMeans.size(); i++) {
			featureMeans.set(i, featureMeans.get(i) / data.numDatapoints());
		}
		for (int i = 0; i < data.numDatapoints(); i++) {
			for (int j = 0; j < data.get(i).getFeatures().length; j++) {
				int oldValue = data.get(i).getFeatures()[j];
				data.get(i).getFeatures()[j] = Tools.toInt(oldValue - featureMeans.get(j));
			}
		}
	}

	private void calculateCovarianceMatrix() {
		int numDimensions = featureMeans.size();
		double[][] covarianceMatrix = new double[numDimensions][numDimensions];

		for (int i = 0; i < covarianceMatrix.length; i++) {
			for (int j = 0; j < covarianceMatrix[0].length; j++) {
				covarianceMatrix[i][j] = calculateCovariance(i, j);
			}
		}
		this.covarianceMatrix = new DenseDoubleMatrix2D(covarianceMatrix);

	}

	private Double calculateCovariance(int dimensionOneIndex, int dimensionTwoIndex) {

		Double covariance = 0.0;
		Double dimensionOneMean = featureMeans.get(dimensionOneIndex);
		Double dimensionTwoMean = featureMeans.get(dimensionTwoIndex);

		for (Datapoint dataPoint : data.datapoints) {
			int dimensionOneValue = dataPoint.getFeatures()[dimensionOneIndex];
			int dimensionTwoValue = dataPoint.getFeatures()[dimensionTwoIndex];
			covariance += ((dimensionOneValue - dimensionOneMean) * (dimensionTwoValue - dimensionTwoMean));
		}
		covariance /= (featureMeans.size() - 1);
		return covariance;
	}

	private void calculateEigenVectors() {
		eigenValueDecomp = new EigenvalueDecomposition(covarianceMatrix);
	}

	private void findPrincipleComponents() {

		DoubleMatrix2D eigenVectors = eigenValueDecomp.getV();

		DoubleMatrix1D eigenValues = eigenValueDecomp.getRealEigenvalues();
		
		/*
		 * sort the eigenvalues and re-arrange the eigenvector matrix at the same time.
		 */
		for (int i = 0; i < eigenValues.size(); i++){
			for (int j = 0; j < eigenValues.size(); j++){
				if (eigenValues.get(j) < eigenValues.get(i)){
					double eigenValueTemp = eigenValues.get(i);
					eigenValues.set(i, eigenValues.get(j));
					eigenValues.set(j, eigenValueTemp);
				
					//swap values in the eigenvector matrix iff j's eigenvalues are smaller than i's.
					for (int k = 0; k < eigenVectors.columns(); k++){
						double eigenVectorTemp = eigenVectors.get(i, k);
						eigenVectors.set(i, k, eigenVectors.get(j, k));
						eigenVectors.set(j, k, eigenVectorTemp);
					}
				}
			}
		}
		

		principleMatrix = eigenVectors;
	}

	public void printMatrix(DoubleMatrix2D mat) {
		for (int i = 0; i < mat.rows(); i++) {
			for (int j = 0; j < mat.columns(); j++) {
				System.out.print(mat.get(i, j) + " ");
			}
			System.out.println();
		}
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public int numPrincipleComponents() {
		return numPrincipleComponents;
	}

}
